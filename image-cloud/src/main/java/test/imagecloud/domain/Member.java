package test.imagecloud.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
public class Member {
    @Column(name="member_id")
    @Id @GeneratedValue
    private Long id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "member")
    private List<ImageMetaData> imageMetaDataList = new ArrayList<>();
}
