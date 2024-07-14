package com.example.test_task.integr.controller;

import com.example.test_task.web.dto.CityWithLogoResponseDto;
import com.example.test_task.web.dto.JwtRequest;
import com.example.test_task.web.dto.JwtResponse;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("City controller tests.")
@Testcontainers
@ContextConfiguration(initializers = {CityControllerTest.DataSourceInitializer.class, CityControllerTest.MinioInitializer.class})
public class CityControllerTest {
    @LocalServerPort
    private Integer port;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();

    private final AtomicReference<String> token = new AtomicReference<>(null);

    @Container
    private static final MinIOContainer minio = new MinIOContainer(DockerImageName.parse("minio/minio:latest"))
            .withEnv("MINIO_ACCESS_KEY", "miniouser")
            .withEnv("MINIO_SECRET_KEY", "miniouser");

    @Container
    private static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.1-alpine"))
            .withReuse(true);

    public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.test.database.replace=none",
                    "spring.datasource.url=" + database.getJdbcUrl(),
                    "spring.datasource.username=" + database.getUsername(),
                    "spring.datasource.password=" + database.getPassword()
            );
        }
    }

    public static class MinioInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "minio.bucket=image",
                    "minio.url=" + minio.getS3URL(),
                    "minio.accessKey=miniouser",
                    "minio.secretKey=miniouser"
            );
        }
    }

    @Test
    void getAllCities_cityExists_returnCity() {
        this.webTestClient
                .get()
                .uri("/api/v1/cities?pageNumber=0&pageSize=1")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(CityWithLogoResponseDto.class)
                .hasSize(1);

    }

    @Test
    void getCityByCityName_cityExists_returnCity() {
        this.webTestClient
                .get()
                .uri("/api/v1/cities/city?cityName=Ashtarak")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(CityWithLogoResponseDto.class);

    }

    @BeforeEach
    public void setUp() throws Exception {
        JwtRequest request = JwtRequest.builder()
                .login("user")
                .password("user")
                .build();
        String accessToken = this.webTestClient
                .post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectBody(JwtResponse.class).returnResult().getResponseBody().getAccessToken();
        token.set(accessToken);
        boolean isExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket("image")
                .build());
        if (!isExists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket("image")
                    .build());
            minioClient.putObject(PutObjectArgs.builder()
                    .stream(new InputStream() {
                        @Override
                        public int read() throws IOException {
                            return 0;
                        }
                    }, 0, -1)
                    .bucket("image")
                    .object("am.png")
                    .build());
        }
    }

    @AfterAll
    static void onDestroy() {
        minio.stop();
        database.stop();
    }
}