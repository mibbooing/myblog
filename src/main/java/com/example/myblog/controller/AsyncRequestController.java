package com.example.myblog.controller;


import com.example.myblog.entity.Category;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RequestMapping("/async")
@Controller
@RequiredArgsConstructor
public class AsyncRequestController {

    private final PostService postService;
    private final BlogService blogService;
    private final FileService fileService;
    private final CategoryService categoryService;

    @PostMapping(value = "/valid/blogNm")
    @ResponseBody
    public ResponseEntity duplicateBlogNm(@RequestBody Map<String, Object> paramMap) {
        if (paramMap.get("blogNm") == null) {
            return new ResponseEntity<String>("잘못된 입력입니다.", HttpStatus.BAD_REQUEST);
        }
        String blogNm = paramMap.get("blogNm").toString();
        try {
            blogService.validateDuplicateBlog(blogNm);
        } catch (IllegalStateException e) {
            System.out.println(blogNm);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(blogNm, HttpStatus.OK);
    }

    @PostMapping(value = "/category/new/main")
    @ResponseBody
    @PreAuthorize("@authorizationChecker.checkBlogAuth(#paramMap.get('blogNm'), principal.username)")
    public ResponseEntity createCategory(@RequestBody Map<String, Object> paramMap) {
        if (paramMap.get("blogNm") == null) {
            return new ResponseEntity<String>("카테고리 생성에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
        String blogNm = paramMap.get("blogNm").toString();
        Category category;
        try {
            category = categoryService.createMainCategory(blogNm);
        } catch (IllegalStateException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        System.out.println(category.getId());
        return new ResponseEntity<Long>(category.getId(), HttpStatus.OK);
    }

    @PostMapping(value = "/posts/upload/{blogNm}")
    @ResponseBody
    @PreAuthorize("@authorizationChecker.checkBlogAuth(#blogNm, principal.username)")
    public ResponseEntity<String> imageUpload(@PathVariable("blogNm") String blogNm, MultipartFile upload, HttpServletResponse res, HttpServletRequest req) {
        if (upload.isEmpty()) {
            return new ResponseEntity<String>("파일이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        String fileName;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            File folder = fileService.makePath("C:/myblog/post/temp/" + sdf.format(now) + "/" + blogNm + "/");
            fileName = fileService.uploadFile(folder.getPath(), upload.getOriginalFilename(), upload.getBytes());
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("url", "/images/post/temp/" + sdf.format(now) + "/" + blogNm + "/" + fileName);
        obj.addProperty("oriImgName", upload.getOriginalFilename());
        obj.addProperty("tempUrl", "/temp/" + sdf.format(now) + "/" + blogNm + "/");
        obj.addProperty("imgName", fileName);
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<>(obj.toString(), header, HttpStatus.OK);
    }

    @PostMapping(value = "/posts/preProcessing/{blogNm}")
    @ResponseBody
    @PreAuthorize("@authorizationChecker.checkBlogAuth(#blogNm, principal.username)")
    public ResponseEntity<String> imageReplacePath(@PathVariable("blogNm") String blogNm, @RequestBody Map<String, Object> paramMap) {
        if (blogNm.isEmpty()) {
            return new ResponseEntity<String>("blogName null!", HttpStatus.BAD_REQUEST);
        }
        JsonObject obj = new JsonObject();
        try {
            Post emptyPost = postService.createPost(blogNm);
            String reqTargetPath = "C:/myblog/post" + (String) paramMap.get("imgTempUrl");
            String reqDestPath = "C:/myblog/post/" + blogNm + "/" + emptyPost.getId() + "/";
            fileService.replaceImgPath(reqTargetPath, reqDestPath);
            String imgUrl = "/images/post/" + blogNm + "/" + emptyPost.getId() + "/";
            System.out.println("PostId : " + imgUrl);
            obj.addProperty("postId", emptyPost.getId());
            obj.addProperty("imgUrl", imgUrl);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }
}
