package com.example.myblog.dto;

import com.example.myblog.constant.PostStatus;
import com.example.myblog.constant.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class CommentDto {
    private Long commentId;
    @NotBlank(message = "내용을 입력해 주세요.")
    @Length(min = 2, max = 500, message = "댓글은 2자 이상, 500자 이하로 작성가능합니다.")
    private String commentContent;

    private Long memberId;

    private String email;

    private String name;

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

    public CommentDto(Long commentId, String commentContent, Long memberId, String email, String name, Long postId, PostStatus commentStatus, Role commentStatusRequester) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.postId = postId;
        this.commentStatus = commentStatus;
        this.commentStatusRequester = commentStatusRequester;
    }


}
