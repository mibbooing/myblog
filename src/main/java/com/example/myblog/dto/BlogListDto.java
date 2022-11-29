package com.example.myblog.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogListDto {

    private Long blogId;

    private String blogNm;

    private Long memberId;

    public BlogListDto(Long blogId, String blogNm,Long memberId){
        this.blogId = blogId;
        this.blogNm = blogNm;
        this.memberId = memberId;
    }
}
