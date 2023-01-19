package com.example.myblog.dto;

import com.example.myblog.constant.PostStatus;
import com.example.myblog.entity.Post;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class PostDto {
    private Long id;

    @NotBlank(message="제목을 입력해주세요.")
    @Size(min = 2, max = 50, message = "제목은 최소 2~50자 사이로 입력해주세요.")
    private String title;

    private Long blogId;

    @NotBlank(message="본문내용을 입력해주세요.")
    private String contentUrl;

    private String previewContents;

    private PostStatus postStatus;

    private Long categoryId;

    private String categoryNm;

    private String regTime;

    public PostDto() {
    }

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.blogId = post.getBlog().getId();
        this.contentUrl = post.getContentUrl();
        this.previewContents = post.getPreviewContents();
        this.postStatus = post.getPostStatus();
        this.categoryId = post.getCategory().getId();
        this.categoryNm = post.getCategory().getCategoryNm();
        this.regTime = post.getRegTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

//    public PostDto(Long id, String title, Long blogId, String contentUrl, PostStatus postStatus, Long categoryId, String categoryNm) {
//        this.id = id;
//        this.title = title;
//        this.blogId = blogId;
//        this.contentUrl = contentUrl;
//        this.postStatus = postStatus;
//        this.categoryId = categoryId;
//        this.categoryNm = categoryNm;
//    }
}
