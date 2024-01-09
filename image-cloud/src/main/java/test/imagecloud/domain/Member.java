package test.imagecloud.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
public class Member {
    @Column(name="member_id")
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "member")
    private List<ImageMetaData> imageMetaDataList = new ArrayList<>();

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void addImageMetaData(ImageMetaData imageMetaData) {
        imageMetaDataList.add(imageMetaData);
    }
}
