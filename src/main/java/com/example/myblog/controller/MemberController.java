package com.example.myblog.controller;

import com.example.myblog.constant.LogType;
import com.example.myblog.dto.BlogListDto;
import com.example.myblog.dto.MemberFormDto;
import com.example.myblog.dto.MemberInfoFormDto;
import com.example.myblog.dto.MemberLogDto;
import com.example.myblog.entity.Member;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    private final BlogService blogService;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
            memberService.saveMemberLog(member, LogType.CREATE);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/home";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm";
    }
    @GetMapping(value = "/myPage")
    public String myPage(Principal principal, Model model){
        try{
            MemberInfoFormDto memberInfoFormDto = memberService.getMemberInfo(principal.getName());
            model.addAttribute("memberInfoFormDto", memberInfoFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "잘못된 접근입니다.");
            return "redirect:/home";
        }

        return "member/myPage";
    }
    @PostMapping(value = "/myPage")
    public String saveMyPage(Principal principal, MemberInfoFormDto memberInfoFormDto, Model model,@RequestParam("memberImgFile") MultipartFile imgFiles){
        try {
            Member member = memberService.updateMemberInfo(principal.getName(), memberInfoFormDto, imgFiles);
            memberService.saveMemberLog(member, LogType.UPDATE);
        } catch (Exception e){
            model.addAttribute("errorMessage", "사용자 정보 수정중 에러가 발생하였습니다.");
            return "member/myPage";
        }
        return "/home";
    }

}
