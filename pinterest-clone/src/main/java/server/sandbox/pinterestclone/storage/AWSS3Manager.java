package server.sandbox.pinterestclone.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import server.sandbox.pinterestclone.domain.dto.FileRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@RequiredArgsConstructor
public class AWSS3Manager implements StorageManager {

    private final S3Client s3Client;
    @Value("${aws.bucketName}")
    private String BUCKET_NAME;
    @Value(("${aws.region}"))
    private String LOCATION;

    @Override
    public String uploadFile(String fileName, FileRequest fileRequest) throws IOException {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .contentType(fileRequest.getContentType())
                .contentLength(fileRequest.getContentLength())
                .build();

        s3Client.putObject(objectRequest, RequestBody.fromBytes(fileRequest.getInputStream().readAllBytes()));

        return getFilePath(fileName);
    }

    private String getFilePath(String fileName) {
        return "https://" + BUCKET_NAME + ".s3." + LOCATION + ".amazonaws.com/" + fileName;
    }

    public void deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}
