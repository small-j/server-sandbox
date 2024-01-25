package test.imagecloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import test.imagecloud.dto.MemberDto;
import test.imagecloud.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public void addMember(@RequestBody MemberDto memberDto) {
        memberService.addMember(memberDto);
    }
}
