package com.example.myblog.dto;

import com.example.myblog.constant.PostStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    private PostStatus postStatus;

    private Long categoryId;
}
