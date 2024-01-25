package test.imagecloud.domain;

import jakarta.persistence.*;
import lombok.Getter;
import test.imagecloud.dto.FileDataDto;
import test.imagecloud.dto.FileMetaDataDto;

import java.util.UUID;

@Entity
@Getter
public class FileMetaData {

    @Id @GeneratedValue
    @Column(name = "image_meta_data_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(name = "file_path")
    private String filePath;

    public FileMetaData(Member member, String filePath) {
        this.member = member;
        this.filePath = filePath;
    }

    public static FileMetaData createImageMetaData(Member member, StorageManager storageManager, FileMetaDataDto fileMetaDataDto, FileDataDto fileDataDto) {
        String imagePath = uploadFile(member, storageManager, fileMetaDataDto,fileDataDto);
        FileMetaData fileMetaData = new FileMetaData(member, imagePath);
        member.addFileMetaData(fileMetaData);

        return fileMetaData;
    }

    private static String uploadFile(Member member, StorageManager storageManager, FileMetaDataDto fileMetaDataDto, FileDataDto fileDataDto) {
        return storageManager.uploadFile(member.getId() + UUID.randomUUID().toString(), fileMetaDataDto, fileDataDto);
    }
}
