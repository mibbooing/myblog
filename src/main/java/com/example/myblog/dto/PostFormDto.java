package com.example.myblog.dto;

import com.example.myblog.constant.PostStatus;
import com.example.myblog.entity.Blog;
import com.example.myblog.entity.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class PostFormDto {

    private String blogNm;

    private String email;

    private PostDto postDto;

    private Blog blog;

    private List<PostImgDto> postImgDtoList = new ArrayList<>();

    private List<CategoryDto> categoryDtoList = new ArrayList<>();

    private List<PostStatus> postStatusList = new ArrayList<>();

    private PostImgDto postImgDto;

    public PostFormDto() {
    }

    public void setPostForm(String blogNm, List<CategoryDto> categoryDtoList, List<PostStatus> postStatusList){
        this.blogNm = blogNm;
        this.categoryDtoList = categoryDtoList;
        this.postStatusList = postStatusList;
        this.postImgDtoList.add(new PostImgDto());
    }

    public void setPostReadData(String email, PostDto postDto, Blog blog, List<CategoryDto> categoryDtoList, PostImgDto postImgDto){
        this.email = email;
        this.postDto = postDto;
        this.blogNm = blog.getBlogNm();
        this.blog = blog;
        this.categoryDtoList = categoryDtoList;
        this.postImgDto = postImgDto;
    }
}
