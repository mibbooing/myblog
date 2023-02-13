package com.example.myblog.entity;

import com.example.myblog.constant.PostStatus;
import com.example.myblog.constant.Role;
import com.example.myblog.dto.CommentDto;
import com.example.myblog.dto.CommentFormDto;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@Getter
@ToString
public class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private PostStatus commentStatus;

    @Enumerated(EnumType.STRING)
    private Role commentStatusRequester;

    public void createComment(CommentFormDto commentFormDto, Member member, Post post) {
        this.content = commentFormDto.getContent();
        this.member = member;
        this.post = post;
        this.commentStatus = commentFormDto.getCommentStatus();
        this.commentStatusRequester = commentFormDto.getCommentStatusRequester();
    }

    public void updateComment(CommentDto commentDto) {
        this.content = commentDto.getCommentContent();
        this.commentStatus = commentDto.getCommentStatus();
        this.commentStatusRequester = commentDto.getCommentStatusRequester();
    }

    public void deleteComment(CommentDto commentDto) {
        this.commentStatus = commentDto.getCommentStatus();
        this.commentStatusRequester = commentDto.getCommentStatusRequester();
    }
}
