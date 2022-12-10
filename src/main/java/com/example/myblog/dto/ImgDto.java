package com.example.myblog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;

    public ImgDto() {}

    public ImgDto(Long id, String imgName, String oriImgName, String imgUrl, String repimgYn) {
        this.id = id;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repimgYn = repimgYn;
    }
    public void updateImgDto(String imgName, String oriImgName, String imgUrl, String repimgYn){
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repimgYn = repimgYn;
    }
}
