package com.example.myblog.dto;


import com.example.myblog.entity.Blog;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;


    public BlogImgDto() {
    }

    public BlogImgDto(Long id, String imgName, String oriImgName, String imgUrl, String repimgYn){
        this.id = id;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repimgYn = repimgYn;
    }
}
