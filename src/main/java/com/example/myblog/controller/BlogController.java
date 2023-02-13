package com.example.myblog.controller;

import com.example.myblog.dto.*;
import com.example.myblog.entity.Category;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequestMapping("/blogs")
@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    private final CategoryService categoryService;

    @GetMapping(value = "/new")
    public String blogForm(BlogFormDto blogFormDto, Model model) {
        blogFormDto = blogService.getBlogForm();
        model.addAttribute("blogFormDto", blogFormDto);
        return "blog/createBlogForm";
    }

    @PostMapping(value = "/new")
    public String blogForm(@Valid BlogFormDto blogFormDto, BindingResult bindingResult, Model model, Principal principal) throws UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            blogFormDto.setTopicList(blogService.getBlogForm().getTopicList());
            model.addAttribute("blogFormDto", blogFormDto);
            Map<String, String> validatorResult = blogService.validateHandler(bindingResult);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "blog/createBlogForm";
        }
        try {
            blogService.saveBlog(blogFormDto, principal.getName());
            categoryService.saveCategory(blogFormDto.getBlogNm(), null);
        } catch (IllegalStateException e) {
            blogFormDto = blogService.getBlogForm();
            model.addAttribute("blogFormDto", blogFormDto);
            model.addAttribute("errorMessage", e.getMessage());
            return "blog/createBlogForm";
        }
        String encodedParam = URLEncoder.encode(blogFormDto.getBlogNm(), "UTF-8");
        return "redirect:/blogs/main/" + encodedParam;
    }

    @GetMapping(value = "/main/{blogNm}")
    public String getBlogMain(@PathVariable("blogNm") String blogNm, @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        BlogMainFormDto blogMainFormDto = blogService.getBlogMain(blogNm, page);
        model.addAttribute("blogMainFormDto", blogMainFormDto);
        model.addAttribute("memberInfoFormDto", blogMainFormDto.getMemberInfoFormDto());
        model.addAttribute("categoryDtoList", blogMainFormDto.getCategoryDtoList());
        model.addAttribute("blogImgDto", blogMainFormDto.getBlogImgDto());
        return "blog/blogForm";
    }

    @GetMapping(value = "/myPage/{blogNm}")
    @PreAuthorize("@authorizationChecker.checkBlogAuth(#blogNm, principal.username)")
    public String getBlogMyPage(@PathVariable("blogNm") String blogNm, Model model, Principal principal) {
        TypeSet logTypeSet = new TypeSet();
        BlogMyPageFormDto blogMyPageFormDto = new BlogMyPageFormDto(blogService.getMyBlogForm(blogNm), categoryService.getCategory(blogNm), logTypeSet.createLogTypeSet());
        model.addAttribute("blogMyPageFormDto", blogMyPageFormDto);
        return "blog/myPage";
    }

    @PostMapping(value = "/myPage/{blogNm}")
    @PreAuthorize("@authorizationChecker.checkBlogAuth(#blogNm, principal.username)")
    public String updateBlogMyPage(@PathVariable("blogNm") String blogNm, @ModelAttribute BlogMyPageFormDto blogMyPageFormDto, @RequestParam("blogImgFile") MultipartFile multipartFile, Model model) {
        System.out.println("블로그ID: " + blogMyPageFormDto.getBlogInfoFormDto().getBlogId());
        List<CategoryDto> list = blogMyPageFormDto.getCategoryDtoList();
        System.out.println(list.size());
        for (CategoryDto a : list) {
            System.out.println("name: " + a.getCategoryNm() + "  Type: " + a.getDepth() + "  sortNum:" + a.getSortNum() + "  blogId:" + a.getBlogId() + "  P_categoryId:" + a.getParentCategoryId() + "  categoryId:" + a.getCategoryId() + " reqType:" + a.getReqType());
        }
        try {
            blogService.updateBlogInfo(blogMyPageFormDto.getBlogInfoFormDto(), multipartFile);
            categoryService.saveCategory(blogNm, blogMyPageFormDto.getCategoryDtoList());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "블로그 정보 수정중 에러가 발생하였습니다.");
            return "blog/myPage";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/category/{categoryNum}")
    public String getPostsList(@PathVariable("categoryNum") String categoryNum, @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        BlogMainFormDto blogMainFormDto = blogService.getPostListCategoryNum(Long.parseLong(categoryNum), page);
        model.addAttribute("blogMainFormDto", blogMainFormDto);
        model.addAttribute("memberInfoFormDto", blogMainFormDto.getMemberInfoFormDto());
        model.addAttribute("categoryDtoList", blogMainFormDto.getCategoryDtoList());
        model.addAttribute("blogImgDto", blogMainFormDto.getBlogImgDto());
        return "blog/blogForm";
    }

}
