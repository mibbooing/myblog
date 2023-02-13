package com.example.myblog.service;

import com.example.myblog.dto.CommentDto;
import com.example.myblog.dto.CommentFormDto;
import com.example.myblog.entity.Comment;
import com.example.myblog.entity.Member;
import com.example.myblog.entity.Post;
import com.example.myblog.repository.CommentRepository;
import com.example.myblog.repository.MemberRepository;
import com.example.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    public CommentFormDto getCommentFormDto() {
        return new CommentFormDto();
    }

    public Page<CommentDto> getCommentDtoList(int page, Long postId, String email) {
        Member member = memberRepository.findByEmail(email);
        Pageable pageable = PageRequest.of(page, 10);
        if (member != null) {
            return commentRepository.findByPostIdAndMemberId(pageable, postId, member.getId());
        } else {
            return commentRepository.findByPostId(pageable, postId);
        }
    }

    public Comment createComment(CommentFormDto commentFormDto, String email) {
        Comment comment = new Comment();
        Member member = memberRepository.findByEmail(email);
        Post post = postRepository.findById(commentFormDto.getPostId()).orElseThrow(() -> new EntityNotFoundException());
        comment.createComment(commentFormDto, member, post);
        return commentRepository.save(comment);
    }

    public void updateComment(CommentDto commentDto, String email) {
        Member member = memberRepository.findByEmail(email);
        Comment comment = commentRepository.findById(commentDto.getCommentId()).orElseThrow(() -> new EntityNotFoundException("COMMENT_NOT_FOUND"));
        if (member.getId().equals(comment.getMember().getId())) {
            comment.updateComment(commentDto);
        }
    }

    public void deleteComment(CommentDto commentDto, String email) {
        Member member = memberRepository.findByEmail(email);
        Comment comment = commentRepository.findById(commentDto.getCommentId()).orElseThrow(() -> new EntityNotFoundException("COMMENT_NOT_FOUND"));
        if (member.getId().equals(comment.getMember().getId()) || member.getId().equals(comment.getPost().getBlog().getMember().getId())) {
            comment.deleteComment(commentDto);
        }
    }
}
