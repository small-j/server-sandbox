package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.sandbox.pinterestclone.domain.converter.RolesAttributeConverter;
import server.sandbox.pinterestclone.service.enums.UserRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class User extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Convert(converter = RolesAttributeConverter.class)
    private List<UserRole> roles;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Image> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<SaveImage> saveImages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserImageHistory> userImageHistories = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
    }

    public void addSaveImage(SaveImage saveImage) { saveImages.add(saveImage); }

    public void addImageHistory(UserImageHistory userImageHistory) {
        userImageHistories.add(userImageHistory);
    }

}
