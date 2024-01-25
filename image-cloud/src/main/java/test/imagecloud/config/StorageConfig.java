package test.imagecloud.config;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.imagecloud.domain.StorageManager;
import test.imagecloud.domain.storageManager.AmazonS3StorageManager;

@Configuration
public class StorageConfig {

    private final AmazonS3 amazonS3Client;

    public StorageConfig(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Bean
    public StorageManager storageManager() {
        return new AmazonS3StorageManager(amazonS3Client);
    }
}
