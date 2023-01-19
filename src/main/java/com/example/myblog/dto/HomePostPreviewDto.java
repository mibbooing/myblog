package com.example.myblog.dto;


import com.example.myblog.entity.Topic;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class HomePostPreviewDto {
    private Long blogId;

    private String blogNm;

    private String blogImgUrl;

    private Long postId;

    private String title;

    private String previewContents;

    //    private int commentCount;
    //
    //    private int likeCount;

    private String postImgUrl;

    private String regTime;

    private Long topicId;

    public HomePostPreviewDto() {
    }

    public HomePostPreviewDto(Long blogId, String blogNm, String blogImgUrl, Long postId, String title, String previewContents, String postImgUrl, LocalDateTime regTime) {
        this.blogId = blogId;
        this.blogNm = blogNm;
        this.blogImgUrl = blogImgUrl;
        this.postId = postId;
        this.title = title;
        this.previewContents = previewContents;
        this.postImgUrl = postImgUrl;
        this.regTime = regTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}
