package com.example.myblog.controller;


import com.example.myblog.dto.BlogFormDto;
import com.example.myblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequestMapping("/valid")
@Controller
@RequiredArgsConstructor
public class ValidController {

    private final BlogService blogService;

    @PostMapping(value = "/blogNm")
    @ResponseBody
    public ResponseEntity duplicateBlogNm(@RequestBody Map<String, Object> paramMap){
            if(paramMap.get("blogNm") == null){
                return new ResponseEntity<String>("잘못된 입력입니다.", HttpStatus.BAD_REQUEST);
            }
            String blogNm = paramMap.get("blogNm").toString();
            try{
                blogService.validateDuplicateBlog(blogNm);
            }catch (IllegalStateException e){
                System.out.println(blogNm);
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<String>(blogNm, HttpStatus.OK);
    }
}
