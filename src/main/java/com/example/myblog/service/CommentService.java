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

    public CommentFormDto getCommentFormDto(){
        return new CommentFormDto();
    }

    public Page<CommentDto> getCommentDtoList(int page, Long postId, String email){
        Member member = memberRepository.findByEmail(email);
        Pageable pageable = PageRequest.of(page, 50);
        return commentRepository.findByPostIdAndMemberId(pageable, postId, member.getId());
    }

    public Comment createComment(CommentDto commentDto){
        Comment comment = new Comment();
        Member member = memberRepository.findById(commentDto.getMemberId()).orElseThrow(()->new EntityNotFoundException());
        Post post = postRepository.findById(commentDto.getPostId()).orElseThrow(()->new EntityNotFoundException());
        comment.createComment(commentDto, member, post);
        return commentRepository.save(comment);
    }
}
