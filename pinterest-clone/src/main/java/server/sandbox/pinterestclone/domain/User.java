package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int id;

    String name;

    String email;

    // TODO: add password.

    @OneToMany(mappedBy = "user")
    List<UserImagePin> userImagePins = new ArrayList<>();
}
