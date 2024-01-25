package test.imagecloud.domain.storageManager;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import test.imagecloud.domain.StorageManager;
import test.imagecloud.dto.FileDataDto;
import test.imagecloud.dto.FileMetaDataDto;

@RequiredArgsConstructor
public class AmazonS3StorageManager implements StorageManager {

    private final AmazonS3 amazonS3Client;
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Override
    public String uploadFile(String fileName, FileMetaDataDto fileMetaDataDto, FileDataDto fileDataDto) {
        this.streamUpload(fileName, fileMetaDataDto, fileDataDto);
        return getUploadFileUrl(fileName);
    }

    private void streamUpload(String fileName, FileMetaDataDto fileMetaDataDto, FileDataDto fileDataDto) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(fileMetaDataDto.getContentType());
        metadata.setContentLength(fileMetaDataDto.getContentLength());

        PutObjectRequest objectRequest = new PutObjectRequest(bucketName, fileName, fileDataDto.getInputStream(), metadata);
        amazonS3Client.putObject(objectRequest);
        // smallj: 위의 업로드가 동기로 처리되나? 동기로 처리되어야지만 file url을 바로 받아올 수 있다.
    }

    private String getUploadFileUrl(String fileName) {
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }
}
