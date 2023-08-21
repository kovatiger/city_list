package com.example.test_task.service.impl;

import com.example.test_task.exception.ImageUploadException;
import com.example.test_task.security.props.MinioProperties;
import com.example.test_task.service.ImageService;
import com.example.test_task.web.dto.CityWithLogoResponseDto;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String getImageUrlByLogoName(String logoName) {
        try {
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(logoName)
                    .build();
            InputStream object = minioClient.getObject(args);

            byte[] content = IOUtils.toByteArray(object);
            return Base64.getEncoder().encodeToString(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CityWithLogoResponseDto> getListOfCitiesWithLogos(List<CityWithLogoResponseDto> citiesWithLogoDto) {
        for (CityWithLogoResponseDto city : citiesWithLogoDto) {
            String logoName = city.getCountry().getLogoName();
            String logoUrl = getImageUrlByLogoName(logoName);
            city.setLogoUrl(logoUrl);
        }
        return citiesWithLogoDto;
    }

    @Override
    public String updateLogo(MultipartFile file, String logoName) {
        String fileName = "";

        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException("Image must have name");
        }

        try {
            createBucket();

            fileName = generateFileName(file, logoName);
            InputStream inputStream = file.getInputStream();
            saveImage(inputStream, fileName);

            deleteExistingLogo(logoName);
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed" + e.getMessage());
        }
        return fileName;
    }

    @SneakyThrows
    private void createBucket() {
        boolean isExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!isExists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    @SneakyThrows
    private void deleteExistingLogo(String logoName) {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(logoName)
                .build();
        minioClient.removeObject(removeObjectArgs);
    }

    private String generateFileName(MultipartFile file, String logoName) {
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void saveImage(InputStream inputStream, String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}