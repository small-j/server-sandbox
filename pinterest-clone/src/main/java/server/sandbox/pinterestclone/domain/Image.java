package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "image_meta")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_meta_id")
    int id;

    String imageUrl;

    String title;

    String content;

    @Builder.Default
    @OneToMany(mappedBy = "image")
    List<UserImageHistory> userImageHistories = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "image")
    List<ImageReply> imageReplies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "image")
    List<ImageCategory> imageCategories = new ArrayList<>();

    public void addCategory(ImageCategory imageCategory) {
        imageCategories.add(imageCategory);
    }

    public String uploadImage(InputStream inputStream) {
        // TODO: 이미지 업로드 기능 추가
        imageUrl = "";
        return "";
    }
}
