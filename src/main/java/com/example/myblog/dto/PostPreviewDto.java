package com.example.myblog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostPreviewDto {
    private Long postId;

    private String title;

    private String content;

    private int commentCount;

    private int likeCount;

    private Long postImgId;

    private String postImgUrl;

    private String postImgNm;

    public PostPreviewDto() {
    }

    public PostPreviewDto(Long postId, String title, String content, int commentCount, int likeCount, Long postImgId, String postImgUrl, String postImgNm) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.postImgId = postImgId;
        this.postImgUrl = postImgUrl;
        this.postImgNm = postImgNm;
    }
}
