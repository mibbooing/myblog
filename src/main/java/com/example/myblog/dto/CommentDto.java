package com.example.myblog.dto;

import com.example.myblog.constant.PostStatus;
import com.example.myblog.constant.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDto {
    private Long commentId;

    private String commentContent;

    private Long memberId;

    private String email;

    private Long postId;

    private PostStatus commentStatus;

    private Role commentStatusRequester;

    public CommentDto() {
    }

    public CommentDto(CommentFormDto commentFormDto) {
        this.commentId = commentFormDto.getCommentId();
        this.commentContent = commentFormDto.getContent();
        this.memberId = commentFormDto.getMemberId();
        this.commentStatus = commentFormDto.getCommentStatus();
        this.commentStatusRequester = commentFormDto.getCommentStatusRequester();
    }

    public CommentDto(Long commentId, String commentContent, Long memberId, String email, Long postId, PostStatus commentStatus, Role commentStatusRequester) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.memberId = memberId;
        this.email = email;
        this.postId = postId;
        this.commentStatus = commentStatus;
        this.commentStatusRequester = commentStatusRequester;
    }


}
