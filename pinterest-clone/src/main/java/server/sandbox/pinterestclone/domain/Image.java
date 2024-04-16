package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    String url;

    @Column(name = "image_key")
    String key;

    String title;

    @Column(name = "image_content")
    String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Builder.Default
    @OneToMany(mappedBy = "image", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<ImageReply> imageReplies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "image", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<ImageCategory> imageCategories = new ArrayList<>();

    public void addCategory(ImageCategory imageCategory) {
        imageCategories.add(imageCategory);
    }

    public void addReply(ImageReply imageReply) { imageReplies.add(imageReply); }

    public void setUser(User user) {
        this.user = user;
        user.addImage(this);
    }
}
