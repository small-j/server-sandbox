package test.imagecloud.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import test.imagecloud.domain.ImageMetaData;
import test.imagecloud.domain.Member;
import test.imagecloud.dto.ImagePathListDto;
import test.imagecloud.dto.MemberDto;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findById(Long memberId) {
        Member member =  em.find(Member.class, memberId);

//        if (member) // optional을 통해서 member 가 없을 경우 처리 해주기.

        return member;
    }

    public List<ImagePathListDto> getAllImagePath(Long memberId) {
        Member member = em.find(Member.class, memberId);

        List<ImagePathListDto> imagePathListDtoList = new ArrayList<>();
        for (ImageMetaData imageMetaData : member.getImageMetaDataList()) {
            imagePathListDtoList.add(new ImagePathListDto(imageMetaData));
        }

        return imagePathListDtoList;
    }
}
