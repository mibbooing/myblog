package com.example.myblog.service;

import com.example.myblog.dto.BlogFormDto;
import com.example.myblog.dto.BlogListDto;
import com.example.myblog.entity.Blog;
import com.example.myblog.entity.Member;
import com.example.myblog.entity.Topic;
import com.example.myblog.repository.BlogRepository;
import com.example.myblog.repository.MemberRepository;
import com.example.myblog.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;

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

    public List<BlogListDto> getBlogMyList(String email){
        Member member = memberRepository.findByEmail(email);
        List<BlogListDto> blogList = blogRepository.findAllBlogDtoList(member.getId());
        return blogList;
    }

    public BlogFormDto getBlogForm(){
        BlogFormDto blogFormDto = new BlogFormDto();
        List<Topic> topicList = topicRepository.findAll();
        blogFormDto.setTopicList(topicList);
        return blogFormDto;
    }

    public Map<String, String> validateHandler(BindingResult bindingResult){
        Map<String, String> validatorResult = new HashMap<>();

        for(FieldError error : bindingResult.getFieldErrors()){
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }
}
