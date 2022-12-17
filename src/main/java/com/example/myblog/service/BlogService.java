package com.example.myblog.service;

import com.example.myblog.constant.CategoryType;
import com.example.myblog.constant.LogType;
import com.example.myblog.dto.*;
import com.example.myblog.entity.*;
import com.example.myblog.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final BlogImgRepository blogImgRepository;

    private final CategoryRepository categoryRepository;
    private final ImgService imgService;

    public Blog saveBlog(BlogFormDto blogFormDto, String email){
        Member member = memberRepository.findByEmail(email);
        validateDuplicateBlog(blogFormDto.getBlogNm());
        if(blogFormDto.getTopicId() == null){
            throw new IllegalStateException("블로그 주제를 선택해주세요.");
        }
        Blog blog = Blog.createBlog(blogFormDto, member, topicRepository.findById(blogFormDto.getTopicId()).orElseThrow(IllegalAccessError::new));
        return blogRepository.save(blog);
    }

    public void validateDuplicateBlog(String BlogNm){
        Blog findBlog = blogRepository.findByBlogNm(BlogNm);
        if(findBlog != null){
            throw new IllegalStateException("이미 존재하는 블로그입니다.");
        }
    }


    public BlogInfoFormDto getMyBlogForm(String blogNm){
        BlogInfoFormDto blogInfoFormDto = blogRepository.findByBlogNmAndRepImgYn(blogNm, "Y");
        List<Topic> topicList = topicRepository.findAll();
        blogInfoFormDto.setTopicList(topicList);
        return blogInfoFormDto;
    }

    public BlogFormDto getBlogForm(){
        BlogFormDto blogFormDto = new BlogFormDto();
        List<Topic> topicList = topicRepository.findAll();
        blogFormDto.setTopicList(topicList);
        return blogFormDto;
    }

    public List<BlogListDto> getMyBlogList(String email){
        Member member = memberRepository.findByEmail(email);
        List<BlogListDto> blogList = blogRepository.findAllBlogDtoList(member.getId());
        return blogList;
    }

    public Map<String, String> validateHandler(BindingResult bindingResult){
        Map<String, String> validatorResult = new HashMap<>();

        for(FieldError error : bindingResult.getFieldErrors()){
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    public List<CategoryDto> getCategory(String blogNm){
        Blog blog = blogRepository.findByBlogNm(blogNm);
        List<CategoryDto> categoryList = categoryRepository.findByBlogId(blog.getId());
        return categoryList;
    }

    public void saveCategory(String blogNm, List<CategoryDto> categoryDtoList){
        Blog blog = blogRepository.findByBlogNm(blogNm);
        Category category;
        Category parentCategory;
        Category mainCategory;
        if(categoryDtoList != null){
            for(CategoryDto categoryDto : categoryDtoList){
                if(categoryDto.getCategoryId() == null){
                    category = new Category();
                    parentCategory = categoryRepository.findById(categoryDto.getParentCategoryId()).orElseThrow(EntityExistsException::new);
                    category.createCategory(categoryDto, blog, parentCategory);
                    categoryRepository.save(category);
                }else if(categoryDto.getCategoryId() != null){
                    if(categoryDto.getReqType() == LogType.UPDATE) {
                        category = categoryRepository.findById(categoryDto.getCategoryId()).orElseThrow(EntityExistsException::new);
                        if(categoryDto.getDepth() == CategoryType.DEFAULT) {
                            category.updateCategory(categoryDto, null);
                        }else{
                            parentCategory = categoryRepository.findById(categoryDto.getParentCategoryId()).orElseThrow(EntityExistsException::new);
                            category.updateCategory(categoryDto, parentCategory);
                        }
                    }else if(categoryDto.getReqType() == LogType.DELETE) {
                        try {
                            category = categoryRepository.findById(categoryDto.getCategoryId()).orElseThrow(EntityExistsException::new);
                        }catch (EntityExistsException e){
                            continue;
                        }
                        categoryRepository.delete(category);
                    }
                }
            }
        }else{
            category = new Category();
            category.createDefaultCategory(blog.getBlogNm(), blog, CategoryType.DEFAULT);
            categoryRepository.save(category);
        }
    }

    public Category createMainCategory(String blogNm){
        Blog blog = blogRepository.findByBlogNm(blogNm);
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDepth(CategoryType.MAIN);
        category.createCategory(categoryDto, blog, categoryRepository.findByBlogIdAndDepth(blog.getId(), CategoryType.DEFAULT));
        return categoryRepository.save(category);
    }

    public BlogInfoFormDto getBlogInfo(String blogNm){
        Blog blog = blogRepository.findByBlogNm(blogNm);
//        BlogInfoFormDto blogInfoFormDto = blogImgRepository.findByBlogIdAndRepimgYn(blog.getId(), "Y");
        BlogInfoFormDto blogInfoFormDto = new BlogInfoFormDto();
        blogInfoFormDto.setTopicList(topicRepository.findAll());
        return blogInfoFormDto;
    }

    public Blog updateBlogInfo(BlogInfoFormDto blogInfoFormDto, MultipartFile imgFiles) throws Exception{
        Blog blog = blogRepository.findById(blogInfoFormDto.getBlogId()).orElseThrow(EntityExistsException::new);
        if(!imgFiles.isEmpty()){
            ImgDto imgDto;
            BlogImg blogImg;
            ImgSaveTypeDto imgSaveTypeDto = new ImgSaveTypeDto(blog.getId(),"blog", blogInfoFormDto.getBlogNm());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("blog", blog);
            if(blogImgRepository.findByBlogIdAndRepimgYn(blog.getId(), "Y") != null){
                blogImg = blogImgRepository.findById(blogInfoFormDto.getBlogId()).get();
                imgDto = modelMapping(blogImg);
                imgService.updateImg(imgDto, imgSaveTypeDto, imgFiles, map);
            }else{
                imgService.saveImg(imgSaveTypeDto, imgFiles, map);
            }
        }
        Topic topic = topicRepository.findById(blogInfoFormDto.getTopicId()).orElseThrow(EntityExistsException::new);
        blog.updateBlog(topic);
        return blog;
    }

    private ImgDto modelMapping(BlogImg blogImg){
            ModelMapper modelMapper = new ModelMapper();
            ImgDto imgDto = modelMapper.map(blogImg, ImgDto.class);
            return imgDto;
    }
}
