package com.example.myblog.controller;

import com.example.myblog.dto.BlogFormDto;
import com.example.myblog.dto.BlogListDto;
import com.example.myblog.dto.MemberImgDto;
import com.example.myblog.dto.MemberInfoFormDto;
import com.example.myblog.entity.Topic;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String getBlogMyPage() {
        return "blog/myPage";
    }

    @PostMapping(value = "/{blogNm}/myPage")
    public String updateBlogMyPage() {
        return "blog/myPage";
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
