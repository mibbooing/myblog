package com.example.myblog.service;

import com.example.myblog.constant.LogType;
import com.example.myblog.dto.MemberImgDto;
import com.example.myblog.dto.MemberInfoFormDto;
import com.example.myblog.entity.Member;
import com.example.myblog.dto.MemberFormDto;
import com.example.myblog.entity.MemberImg;
import com.example.myblog.entity.MemberLog;
import com.example.myblog.repository.MemberImgRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberImgRepository memberImgRepository;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setPassword("1234");
        memberFormDto.setIntroduction("반가워요!");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    public MemberInfoFormDto createMemberInfoFormDto(){
        MemberInfoFormDto memberInfoFormDto = new MemberInfoFormDto();
        memberInfoFormDto.setName("김길순");
        memberInfoFormDto.setIntroduction("안녕하세요!");
        return memberInfoFormDto;
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getIntroduction(), savedMember.getIntroduction());
        assertEquals(member.getRole(), savedMember.getRole());

    }

    @Test
    @DisplayName("중복회원가입 테스트")
    public void saveDuplicateMemberTest() {
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, ()->{
           memberService.saveMember(member2);
        });

        assertEquals("이미 가입된 회원입니다.", e.getMessage());

    }

    @Test
    @DisplayName("회원로그생성 테스트")
    public void saveMemberLogTest(){
        Member member = createMember();
        memberService.saveMember(member);

        MemberLog memberLog = memberService.saveMemberLog(member, LogType.CREATE);

        assertEquals(member.getName(),memberLog.getMember_name_log());
        assertEquals(member.getIntroduction(),memberLog.getMember_introduction_log());
        assertEquals(member.getName(),memberLog.getMember_profile_img_log());
        assertEquals(LogType.CREATE, memberLog.getLogType());
    }
/*
    @Test
    @DisplayName("회원정보 수정 테스트")
    public void updateMemberInfoTest(){
        Member member = createMember();
        MemberInfoFormDto memberInfoFormDto = createMemberInfoFormDto();
        Member savedMember = memberService.saveMember(member);
        Member updatedMember = memberService.updateMemberInfo(member.getEmail(),memberInfoFormDto);

        assertEquals(savedMember.getName(),updatedMember.getName());
        assertEquals(savedMember.getIntroduction(), updatedMember.getIntroduction());
    }*/

    @Test
    @DisplayName("회원정보 수정, 이미지 업로드 테스트")
    public void updateMemberInfoTest() throws Exception{
        Member member = createMember();
        MemberInfoFormDto memberInfoFormDto = createMemberInfoFormDto();
        MockMultipartFile multipartFile = new MockMultipartFile("C:/myblog/member/", "image.jpg","image/jpg",new byte[]{1,2,3,4});

        memberService.saveMember(member);
        Member member1 = memberService.updateMemberInfo(member.getEmail(),memberInfoFormDto,multipartFile);
        MemberInfoFormDto savedMemberInfoFormDto = memberImgRepository.findByMemberIdAndRepimgYn(member.getId(),"Y");
//
        assertEquals(savedMemberInfoFormDto.getImgName(), multipartFile.getOriginalFilename());
    }

}