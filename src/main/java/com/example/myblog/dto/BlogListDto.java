package com.example.myblog.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogListDto {

    private Long id;
    private String blogNm;
    private String imgUrl;

    public BlogListDto() {
    }

    public BlogListDto(Long id, String blogNm, String imgUrl){
        this.id = id;
        this.blogNm = blogNm;
        this.imgUrl = imgUrl;
    }
}
