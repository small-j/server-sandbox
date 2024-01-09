package test.imagecloud.repository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import test.imagecloud.domain.ImageMetaData;
import test.imagecloud.domain.Member;
import test.imagecloud.dto.ImagePathListDto;
import test.imagecloud.dto.MemberDto;

import java.util.List;


@SpringBootTest
@Transactional
class MemberRepositoryTest {

    MemberRepository memberRepository;

    @Autowired
    public MemberRepositoryTest(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Test
    public void 회원정보저장() {
        // given
        MemberDto memberDto = new MemberDto("spring1", "spring1@naver.com");
        Member member = new Member(memberDto.getName(), memberDto.getEmail());

        // when
        memberRepository.save(member);

        // then
        Member findMember = memberRepository.findById(member.getId()); // memberId를 어떻게 가져오지?
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}