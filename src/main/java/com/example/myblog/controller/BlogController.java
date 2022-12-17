package com.example.myblog.controller;

import com.example.myblog.constant.LogType;
import com.example.myblog.dto.*;
import com.example.myblog.entity.Category;
import com.example.myblog.entity.Topic;
import com.example.myblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequestMapping("/blogs")
@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping(value = "/new")
    public String blogForm(BlogFormDto blogFormDto,Model model){
        blogFormDto = blogService.getBlogForm();
        model.addAttribute("blogFormDto", blogFormDto);
        return "blog/blogForm";
    }

    @PostMapping(value = "/new")
    public String blogForm(@Valid BlogFormDto blogFormDto, BindingResult bindingResult, Model model, Principal principal){
        System.out.println(blogFormDto.toString());
        for (Topic id : blogFormDto.getTopicList()) {
                        System.out.println("토픽 있다 : " + id.getId());
                    }
        if(bindingResult.hasErrors()){
            blogFormDto.setTopicList(blogService.getBlogForm().getTopicList());
            model.addAttribute("blogFormDto", blogFormDto);
            Map<String, String> validatorResult = blogService.validateHandler(bindingResult);
            for(String key : validatorResult.keySet()){
                model.addAttribute(key, validatorResult.get(key));
            }
            return "blog/blogForm";
        }
        try {
            blogService.saveBlog(blogFormDto, principal.getName());
            blogService.saveCategory(blogFormDto.getBlogNm(), null);
        } catch (IllegalStateException e){
            blogFormDto = blogService.getBlogForm();
            model.addAttribute("blogFormDto", blogFormDto);
            model.addAttribute("errorMessage", e.getMessage());
            return "blog/blogForm";
        }
        return "redirect:/home";
    }

    @GetMapping(value = "/{blogNm}")
    public String getBlogMain(){
        return "blog/main";
    }

    @GetMapping(value = "/{blogNm}/myPage")
    public String getBlogMyPage(@PathVariable("blogNm")String blogNm,Model model) {
        LogTypeSet logTypeSet = new LogTypeSet();
        BlogMyPageFormDto blogMyPageFormDto = new BlogMyPageFormDto(blogService.getMyBlogForm(blogNm), blogService.getCategory(blogNm), logTypeSet.createLogTypeSet());

        model.addAttribute("blogMyPageFormDto", blogMyPageFormDto);
        return "blog/myPage";
    }

    @PostMapping(value = "/{blogNm}/myPage")
    public String updateBlogMyPage(@PathVariable("blogNm")String blogNm, @ModelAttribute BlogMyPageFormDto blogMyPageFormDto, @RequestParam("blogImgFile") MultipartFile multipartFile, Model model) {
        System.out.println("블로그ID: " + blogMyPageFormDto.getBlogInfoFormDto().getBlogId());
        List<CategoryDto> list = blogMyPageFormDto.getCategoryDtoList();
        try{
            blogService.updateBlogInfo(blogMyPageFormDto.getBlogInfoFormDto(), multipartFile);
            blogService.saveCategory(blogNm, blogMyPageFormDto.getCategoryDtoList());
        } catch (Exception e){
            model.addAttribute("errorMessage", "블로그 정보 수정중 에러가 발생하였습니다.");
            return "blog/myPage";
        }
        return "redirect:/home";
    }

    @PostMapping(value = "/category/new")
    @ResponseBody
    public ResponseEntity createCategory(@RequestBody Map<String, Object> paramMap) {
        if(paramMap.get("blogNm") == null){
            return new ResponseEntity<String>("카테고리 생성에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
        String blogId = paramMap.get("blogNm").toString();
        Category category;
        try{
            category = blogService.createMainCategory(blogId);
        }catch (IllegalStateException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(category.getId(), HttpStatus.OK);
    }

    @GetMapping(value = "/{blogNm}/{category}")
    public String getPostsList(){
        return "blog/list";
    }

    @GetMapping(value = "/posts/new")
    public String postsForm(){
        return "blog/newPosts";
    }

    @PostMapping(value = "/posts/new")
    public String createPosts(){
        return "blog/list";
    }

    @PatchMapping(value = "/{blogNm}/{postsId}")
    public String updatePosts(){
        return "blog/list";
    }

    @DeleteMapping(value = "/{blogNm}/{postsId}")
    public String deletePosts(){
        return "blog/list";
    }

}
