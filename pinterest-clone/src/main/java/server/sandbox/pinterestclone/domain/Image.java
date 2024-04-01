package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "image_meta")
@NoArgsConstructor
@Getter
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_meta_id")
    int id;

    String imageUrl;

    @OneToMany(mappedBy = "image")
    List<UserImageHistory> userImageHistories = new ArrayList<>();

    @OneToMany(mappedBy = "image")
    List<ImageReply> ImageReplies = new ArrayList<>();
}
