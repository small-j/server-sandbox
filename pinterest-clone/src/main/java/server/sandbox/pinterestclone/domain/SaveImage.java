package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "IMAGE_USER_UNIQUE",
                        columnNames = {"image_meta_id", "user_id"}
                )
        }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveImage extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "save_image_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_meta_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static SaveImage createSaveImage(User user, Image image) {
        SaveImage saveImage = SaveImage.builder()
                .user(user)
                .image(image)
                .build();

        user.addSaveImage(saveImage);
        return saveImage;
    }
}
