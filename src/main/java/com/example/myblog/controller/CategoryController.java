//package com.example.myblog.controller;
//
//import com.example.myblog.entity.Category;
//import com.example.myblog.service.CategoryService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.Map;
//
//@RequestMapping("/category")
//@Controller
//@RequiredArgsConstructor
//public class CategoryController {
//
//    private final CategoryService categoryService;
//
//    @PostMapping(value = "/new/main")
//    @ResponseBody
//    public ResponseEntity createCategory(@RequestBody Map<String, Object> paramMap) {
//        if(paramMap.get("blogNm") == null){
//            return new ResponseEntity<String>("카테고리 생성에 실패하였습니다.", HttpStatus.BAD_REQUEST);
//        }
//        String blogNm = paramMap.get("blogNm").toString();
//        Category category;
//        try{
//            category = categoryService.createMainCategory(blogNm);
//        }catch (IllegalStateException e){
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//        System.out.println(category.getId());
//        return new ResponseEntity<Long>(category.getId(), HttpStatus.OK);
//    }
//}
