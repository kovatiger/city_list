# CITY LIST APP

App allows user browse through cities and countries list

# Configuration

### Configure .env file

Create .env file in the root of the project        
Example:
1. POSTGRES_HOST=postgres
2. POSTGRES_USERNAME=postgres
3. POSTGRES_PASSWORD=root
4. POSTGRES_DATABASE=cities
5. POSTGRES_SCHEMA=public
6. JWT_SECRET=ZnRjeXZ5aGJ1aG51am5qTkhVQkdZVmdpam5qaW5idWh2eXY4eWdiOA==
7. JWT_ACCESS_TIME=3600000
8. JWT_REFRESH_TIME=2592000000
9. MINIO_BUCKET=image
10. MINIO_URL=http://minio:9000
11. MINIO_ACCESS_KEY=miniouser
12. MINIO_SECRET_KEY=miniouser

### Docker for installing and running app

Open CLI in the root directory of project.   

To build app use: docker-compose build   
To start app use: docker-compose up

### Fill in Minio logos data

Go http://localhost:9090 and sign in with credentials from .env  
Then create bucket (the same as in .env file) and upload images from logos directory

### Available urls:

http://localhost:8080 - app

http://localhost:8080/swagger-ui/index.html - swagger doc

http://localhost:9090 - minio


