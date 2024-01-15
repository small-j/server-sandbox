package test.imagecloud.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.imagecloud.domain.Member;
import test.imagecloud.dto.MemberDto;
import test.imagecloud.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void addMember(MemberDto memberDto) {
        Member member = new Member(memberDto.getName(), memberDto.getEmail());

        memberRepository.save(member);
    }
}
