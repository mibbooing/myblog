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

    public PostImgDto(Long id, Long postId, String imgName, String oriImgName, String imgUrl, String repimgYn) {
        this.id = id;
        this.postId = postId;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repimgYn = repimgYn;
    }
}
