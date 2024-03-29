package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ImageReply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_reply")
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_meta_id")
    Image image;

    int userId;

    String content;
}
