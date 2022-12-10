package com.example.myblog.service;

import com.example.myblog.constant.LogType;
import com.example.myblog.dto.*;
import com.example.myblog.entity.Member;
import com.example.myblog.entity.MemberImg;
import com.example.myblog.entity.MemberLog;
import com.example.myblog.repository.MemberImgRepository;
import com.example.myblog.repository.MemberLogRepository;
import com.example.myblog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberLogRepository memberLogRepository;
    private final ImgService imgService;
    private final MemberImgRepository memberImgRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    public MemberLog saveMemberLog(Member member, String imgName,LogType logType) {
        MemberLogDto memberLogDto = new MemberLogDto(member.getName(), member.getIntroduction(), imgName, logType);
        MemberLog memberLog = MemberLog.createMemberLog(member, memberLogDto);
        return memberLogRepository.save(memberLog);
    }

    public MemberInfoFormDto getMemberInfo(String email) {
        Member member = memberRepository.findByEmail(email);
        MemberInfoFormDto memberInfoFormDto = memberImgRepository.findByMemberIdAndRepimgYn(member.getId(), "Y");
        if (memberInfoFormDto == null) {
            memberInfoFormDto = new MemberInfoFormDto();
            memberInfoFormDto.setEmail(member.getEmail());
            memberInfoFormDto.setName(member.getName());
            memberInfoFormDto.setIntroduction(member.getIntroduction());
        }
        return memberInfoFormDto;
    }

    public Member updateMemberInfo(String email, MemberInfoFormDto memberInfoFormDto, MultipartFile imgFiles) throws Exception {
        Member member = memberRepository.findByEmail(email);
        if(!imgFiles.isEmpty()) {
            ImgDto imgDto;
            MemberImg memberImg;
            ImgSaveTypeDto imgSaveTypeDto = new ImgSaveTypeDto(member.getId(),"member",email);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("member", member);
            if (memberImgRepository.findByMemberIdAndRepimgYn(member.getId(), "Y") != null) {
                memberImg = memberImgRepository.findById(memberInfoFormDto.getId()).orElseThrow(EntityExistsException::new);
                imgDto = modelMapping(memberImg);
                imgService.updateImg(imgDto, imgSaveTypeDto, imgFiles, map);
            }
            imgService.saveImg(imgSaveTypeDto, imgFiles, map);

        }
        member.updateMemberInfo(memberInfoFormDto.getName(), memberInfoFormDto.getIntroduction());
        return member;
    }

    private ImgDto modelMapping(MemberImg memberImg){
        ModelMapper modelMapper = new ModelMapper();
        ImgDto imgDto = modelMapper.map(memberImg, ImgDto.class);
        return imgDto;
    }
}
