package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
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
    private int id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String roles;

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    @Builder.Default
    @OneToMany(mappedBy = "user")
    List<Image> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    List<SaveImage> saveImages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    List<UserImageHistory> userImageHistories = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
    }

    public void addSaveImage(SaveImage saveImage) { saveImages.add(saveImage); }

    public void addImageHistory(UserImageHistory userImageHistory) {
        userImageHistories.add(userImageHistory);
    }

}
