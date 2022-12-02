package com.example.myblog.controller;

import com.example.myblog.dto.BlogFormDto;
import com.example.myblog.dto.BlogListDto;
import com.example.myblog.dto.MemberImgDto;
import com.example.myblog.dto.MemberInfoFormDto;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/blogs")
@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping(value = "/new")
    public String blogForm(Model model){
        model.addAttribute("blogFormDto", new BlogFormDto());
        return "blog/blogForm";
    }

    @PostMapping(value = "/new")
    public String blogForm(@Valid BlogFormDto blogFormDto, BindingResult bindingResult, Model model, Principal principal){
        if(bindingResult.hasErrors()){
            return "blog/blogForm";
        }

        try {
            blogService.saveBlog(blogFormDto, principal.getName());
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            System.out.println(e.getMessage());
            return "blog/blogForm";
        }
        return "/home";
    }



}
