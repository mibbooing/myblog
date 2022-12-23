package com.example.myblog.controller;


import com.example.myblog.dto.PostDto;
import com.example.myblog.dto.PostFormDto;
import com.example.myblog.dto.PostImgDto;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.CategoryService;
import com.example.myblog.service.FileService;
import com.example.myblog.service.PostService;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RequestMapping("/posts")
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final BlogService blogService;
    private final FileService fileService;

    @GetMapping(value = "/{blogNm}/new")
    public String postForm(@PathVariable("blogNm")String blogNm, Model model){
        model.addAttribute("postFormDto", postService.getPostForm(blogNm));
        return "blog/postForm";
    }

    @PostMapping(value = "/{blogNm}/new")
    public String savePost(@PathVariable("blogNm")String blogNm, @Valid PostFormDto postFormDto, BindingResult bindingResult, MultipartFile multipartFile, Model model){
        for(PostImgDto list : postFormDto.getPostImgDtoList()) {
            System.out.println(list.getImgName());
        }
        if(bindingResult.hasErrors()){
            model.addAttribute("blogFormDto", postService.getPostForm(blogNm));
            Map<String, String> validatorResult = blogService.validateHandler(bindingResult);
            for(String key : validatorResult.keySet()){
                model.addAttribute(key, validatorResult.get(key));
            }
            return "blog/postForm";
        }
        try{
            postService.savePost(postFormDto, multipartFile);
        } catch (IllegalStateException e){
            model.addAttribute("blogFormDto", postService.getPostForm(blogNm));
            model.addAttribute("errorMessage", e.getMessage());
            return "blog/postForm";
        }
        return "redirect:/home";
    }

    @PostMapping(value = "/{blogNm}/upload")
    @ResponseBody
    public ResponseEntity<String> imageUpload(@PathVariable("blogNm")String blogNm, MultipartFile upload, HttpServletResponse res, HttpServletRequest req){
        if(upload.isEmpty()){
            return new ResponseEntity<String>("파일이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        String fileName;
        try{
            fileName = fileService.uploadFile("C:/myblog/member", upload.getOriginalFilename(), upload.getBytes());
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("url","/images/member/"+fileName);
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<>(obj.toString(),header,HttpStatus.OK);
    }
}
