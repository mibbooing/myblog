package com.example.myblog.service;

import com.example.myblog.dto.BlogFormDto;
import com.example.myblog.dto.BlogListDto;
import com.example.myblog.entity.Blog;
import com.example.myblog.entity.Member;
import com.example.myblog.repository.BlogRepository;
import com.example.myblog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final MemberRepository memberRepository;

    public Blog saveBlog(BlogFormDto blogFormDto, String email){
        Member member = memberRepository.findByEmail(email);
        validateDuplicateBlog(blogFormDto);
        Blog blog = Blog.createBlog(blogFormDto, member);
        return blogRepository.save(blog);
    }

    public void validateDuplicateBlog(BlogFormDto blogFormDto){
        Blog findBlog = blogRepository.findByBlogNm(blogFormDto.getBlogNm());
        if(findBlog != null){
            throw new IllegalStateException("이미 존재하는 블로그입니다.");
        }
    }

    public List<BlogListDto> getBlogMyList(String email){
        Member member = memberRepository.findByEmail(email);
        List<BlogListDto> blogList = blogRepository.findAllBlogDtoList(member.getId());
        return blogList;
    }

}
