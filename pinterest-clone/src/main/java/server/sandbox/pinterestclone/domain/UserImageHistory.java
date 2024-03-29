package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class UserImageHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_image_history_id")
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_meta_id")
    Image image;

    int userId;
}
