package test.imagecloud.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import test.imagecloud.storageMapper.StorageManager;

@Entity
@Getter
@AllArgsConstructor
public class ImageMetaData {

    @Id @GeneratedValue
    @Column(name = "image_meta_data_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(name = "image_path")
    private String imagePath;

    public String uploadImageToStorage(StorageManager storage) {
        imagePath = storage.uploadImage();
        return imagePath;
    }
}
