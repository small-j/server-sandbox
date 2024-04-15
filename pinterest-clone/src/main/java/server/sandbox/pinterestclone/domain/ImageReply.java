package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageReply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_reply")
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_meta_id")
    Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    String content;

    public static ImageReply create(Image image, User user, String content) {
        ImageReply imageReply = ImageReply.builder()
                .image(image)
                .user(user)
                .content(content)
                .build();

        image.addReply(imageReply);

        return imageReply;
    }
}
