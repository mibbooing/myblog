package com.example.myblog.controller;


import com.example.myblog.dto.PostDto;
import com.example.myblog.dto.PostFormDto;
import com.example.myblog.dto.PostImgDto;
import com.example.myblog.entity.Post;
import com.example.myblog.service.*;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RequestMapping("/posts")
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final BlogService blogService;
    private final CommentService commentService;

    @GetMapping(value = "/new/{blogNm}")
    @PreAuthorize("@authorizationChecker.checkBlogAuth(#blogNm, principal.username)")
    public String postForm(@PathVariable("blogNm")String blogNm, HttpServletRequest request, Model model){
        try{
            model.addAttribute("postFormDto", postService.getPostForm(blogNm));
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            if(request.getHeader("Referer") != null){
                model.addAttribute("url", request.getHeader("Referer"));
            }
            return "/error/error";
        }
        return "blog/postForm";
    }

    @PostMapping(value = "/new/{blogNm}")
    @PreAuthorize("@authorizationChecker.checkBlogAuth(#blogNm, principal.username)")
    public String savePost(@PathVariable("blogNm")String blogNm, @Valid PostFormDto postFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("blogFormDto", postService.getPostForm(blogNm));
            Map<String, String> validatorResult = blogService.validateHandler(bindingResult);
            for(String key : validatorResult.keySet()){
                model.addAttribute(key, validatorResult.get(key));
            }
            return "blog/postForm";
        }
        try{
            postService.savePost(postFormDto);
        } catch (Exception e){
            model.addAttribute("blogFormDto", postService.getPostForm(blogNm));
            model.addAttribute("errorMessage", e.getMessage());
            return "blog/postForm";
        }
        return "redirect:/";
    }
    @GetMapping(value = "/details/{postNum}")
    @PreAuthorize("@authorizationChecker.checkPostAuth(#postNum, isAnonymous() ? null : principal.username)")
    public String getPostDetail(@PathVariable("postNum")Long postNum, @RequestParam(value = "page", defaultValue = "0") int page,Model model, Principal principal){
        PostFormDto postFormDto = postService.getPost(postNum);
        model.addAttribute("postFormDto", postFormDto);
        model.addAttribute("memberInfoFormDto", blogService.getBlogAuthMemberInfo(postFormDto.getBlog()));
        model.addAttribute("categoryDtoList", postFormDto.getCategoryDtoList());
        model.addAttribute("commentDtoList", commentService.getCommentDtoList(page, postNum, principal.getName()));
        if(principal.getName() != null){
            model.addAttribute("commentFormDto", commentService.getCommentFormDto());
        }
        if(postFormDto.getPostImgDto() == null){
            model.addAttribute("blogImgDto", blogService.getBlogRepImg(postFormDto.getBlog().getId()));
        }
        return "blog/postRead";
    }

}
