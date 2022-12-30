package com.example.myblog.controller;


import com.example.myblog.dto.PostDto;
import com.example.myblog.dto.PostFormDto;
import com.example.myblog.dto.PostImgDto;
import com.example.myblog.entity.Post;
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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RequestMapping("/posts")
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final BlogService blogService;
    private final FileService fileService;

    @GetMapping(value = "/new/{blogNm}")
    public String postForm(@PathVariable("blogNm")String blogNm, Model model){
        try{
            model.addAttribute("postFormDto", postService.getPostForm(blogNm));
        }catch (Exception e){

        }


        return "blog/postForm";
    }

    @PostMapping(value = "/new/{blogNm}")
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
        return "redirect:/home";
    }

    @PostMapping(value = "/upload/{blogNm}")
    @ResponseBody
    public ResponseEntity<String> imageUpload(@PathVariable("blogNm")String blogNm, MultipartFile upload, HttpServletResponse res, HttpServletRequest req){
        if(upload.isEmpty()){
            return new ResponseEntity<String>("파일이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        String fileName;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try{
            File folder = fileService.makePath("C:/myblog/post/temp/"+sdf.format(now)+"/"+blogNm+"/");
            fileName = fileService.uploadFile(folder.getPath(), upload.getOriginalFilename(), upload.getBytes());
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("url","/images/post/temp/"+sdf.format(now)+"/"+blogNm+"/"+fileName);
        obj.addProperty("oriImgName", upload.getOriginalFilename());
        obj.addProperty("tempUrl", "/temp/"+sdf.format(now)+"/"+blogNm+"/");
        obj.addProperty("imgName", fileName);
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<>(obj.toString(),header,HttpStatus.OK);
    }

    @PostMapping(value = "/preProcessing/{blogNm}")
    @ResponseBody
    public ResponseEntity<String> imageReplacePath(@PathVariable("blogNm")String blogNm, @RequestBody Map<String, Object> paramMap){
        if(blogNm.isEmpty()){
            return new ResponseEntity<String>("blogName null!", HttpStatus.BAD_REQUEST);
        }
        JsonObject obj = new JsonObject();
        try{
            Post emptyPost = postService.createPost(blogNm);
            String reqTargetPath = "C:/myblog/post/"+(String)paramMap.get("imgTempUrl")+"/";
            String reqDestPath = "C:/myblog/post/"+blogNm+"/"+emptyPost.getId()+"/";
            fileService.replaceImgPath(reqTargetPath, reqDestPath);
            String imgUrl = "/images/post/"+blogNm+"/"+emptyPost.getId()+"/";
            System.out.println("PostId : "+imgUrl);
            obj.addProperty("postId", emptyPost.getId());
            obj.addProperty("imgUrl", imgUrl);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }
}
