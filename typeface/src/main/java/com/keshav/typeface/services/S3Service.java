package com.keshav.typeface.services;

import com.keshav.typeface.domains.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${supabase.s3.bucket-name}")
    private String bucketName;

    public S3Service(@Value("${supabase.access-key}") String accessKey,
                     @Value("${supabase.secret-key}") String secretKey,
                     @Value("${supabase.s3.endpoint}") String endpoint,
                     @Value("${supabase.s3.region}") String region) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .endpointOverride(URI.create(endpoint))
                .forcePathStyle(true)
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Upload to S3
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

        s3Client.listBuckets();


        return fileName;
    }

    public List<FileInfo> getListOfFiles() {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        
        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        return listResponse.contents().stream()
                .filter(s3Object -> !s3Object.key().equalsIgnoreCase(".emptyFolderPlaceholder"))
                .map(s3Object -> {
                    return new FileInfo(s3Object.key(), s3Object.size(), String.valueOf(s3Object.lastModified()));
                }).toList();

    }

    public byte[] downloadFile(String fileName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();


        InputStream inputStream = s3Client.getObject(getObjectRequest);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Read the input stream into byte array
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toByteArray();

    }

    public String deleteFile(String fileName) throws IOException{
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);

        return fileName;
    }

}

