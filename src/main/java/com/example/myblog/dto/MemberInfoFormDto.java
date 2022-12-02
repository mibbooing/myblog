package com.example.myblog.dto;

import com.example.myblog.entity.Member;
import com.example.myblog.entity.MemberImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MemberInfoFormDto {

    private String email;

    private String name;

    private String introduction;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;

    public MemberInfoFormDto() {}

    public MemberInfoFormDto(String email, String name, String introduction, String imgName, String oriImgName, String imgUrl, String repimgYn) {
        this.email = email;
        this.name = name;
        this.introduction = introduction;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repimgYn = repimgYn;
    }
}
