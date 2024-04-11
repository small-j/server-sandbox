package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int id;

    String name;

    @Column(unique = true)
    String email;

    // TODO: add password.

    @Builder.Default
    @OneToMany(mappedBy = "user")
    List<Image> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    List<SaveImage> saveImages = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
    }

    public void addSaveImage(SaveImage saveImage) { saveImages.add(saveImage); }
}
