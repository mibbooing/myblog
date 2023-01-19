package com.example.myblog.dto;

import com.example.myblog.constant.PostStatus;
import com.example.myblog.constant.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentFormDto {

    private Long commentId;

    private String content;

    private Long memberId;

    private PostStatus commentStatus;

    private Role commentStatusRequester;

}
