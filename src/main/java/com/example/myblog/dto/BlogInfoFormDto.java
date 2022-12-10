package com.example.myblog.dto;

import com.example.myblog.entity.Topic;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BlogInfoFormDto {

    private Long blogId;

    private Long blogImgId;

    private String blogNm;

    private Long topicId;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;

    private List<Topic> topicList = new ArrayList<>();

    public BlogInfoFormDto() {
    }

    public BlogInfoFormDto(Long blogId, Long blogImgId, String blogNm, Long topicId, String imgName, String oriImgName, String imgUrl, String repimgYn) {
        this.blogId = blogId;
        this.blogImgId = blogImgId;
        this.blogNm = blogNm;
        this.topicId = topicId;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repimgYn = repimgYn;
    }
}
