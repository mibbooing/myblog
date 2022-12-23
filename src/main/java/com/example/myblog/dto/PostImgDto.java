package com.example.myblog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostImgDto {
    private Long id;

    private Long postId;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;
}
