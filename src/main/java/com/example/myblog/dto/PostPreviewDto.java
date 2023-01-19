package com.example.myblog.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class PostPreviewDto {
    private Long postId;

    private String title;

    private String contents;

    private String previewContents;

//    private int commentCount;
//
//    private int likeCount;

    private Long postImgId;

    private String postImgUrl;

    private String postImgNm;

    private String regTime;

    public PostPreviewDto() {
    }

//    public PostPreviewDto(Long postId, String title, String content, int commentCount, int likeCount, Long postImgId, String postImgUrl, String postImgNm) {
//        this.postId = postId;
//        this.title = title;
//        this.content = content;
//        this.commentCount = commentCount;
//        this.likeCount = likeCount;
//        this.postImgId = postImgId;
//        this.postImgUrl = postImgUrl;
//        this.postImgNm = postImgNm;
//    }

    public PostPreviewDto(Long postId, String title, String contents, String previewContents, Long postImgId, String postImgUrl, String postImgNm, LocalDateTime regTime) {
        this.postId = postId;
        this.title = title;
        this.contents = contents;
        this.previewContents = previewContents;
        this.postImgId = postImgId;
        this.postImgUrl = postImgUrl;
        this.postImgNm = postImgNm;
        this.regTime = regTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}
