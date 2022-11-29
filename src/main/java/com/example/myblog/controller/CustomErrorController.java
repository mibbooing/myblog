package com.example.myblog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/errors")
public class CustomErrorController {

    @GetMapping(value = "/denied")
    public String denied(Model model) {
        model.addAttribute("errorMessage", "로그인 후 사용 가능합니다. 로그인 페이지로 이동합니다.");
        model.addAttribute("url", "/members/login");
        return "/error/error";
    }

    @GetMapping(value = "/permission")
    public String noPermission(Model model) {
        model.addAttribute("errorMessage", "해당 페이지 접근권한이 없습니다. 메인페이지로 이동합니다.");
        model.addAttribute("url", "/");
        return "/error/error";
    }
}
