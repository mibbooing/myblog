package com.example.myblog.config;


import com.example.myblog.constant.PostStatus;
import com.example.myblog.constant.Role;
import com.example.myblog.entity.*;
import com.example.myblog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class AuthorizationChecker {
    /*
     * 추가될 기능에 대한(COMMENT, ??) 권한 인가 로직 작성 필요!
     * */

    private final MemberRepository memberRepository;
    private final BlogRepository blogRepository;
    private final PostRepository postRepository;
    private final BlogAuthRepository blogAuthRepository;

    private final CommentRepository commentRepository;
    private final AccessAuthRepository accessAuthRepository;

    public boolean checkBlogAuth(String blogNm, String username) throws NullPointerException {
        if (username.isEmpty()) {
            return false;
        }
        try {
            BlogAuth blogAuth = blogAuthRepository.findByBlogNmAndMemberEmail(blogNm, username);
            if (blogAuth.getRole().equals(Role.ADMIN)) {
                return true;
            }
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                throw new NullPointerException("ERR_BLOG_PERMISSION");
            }
        }
        return false;
    }

    public boolean checkPostAuth(Long postId, String username) throws NullPointerException, EntityNotFoundException, AccessDeniedException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("ERR_POST_NUM"));
        if (post.getPostStatus().equals(PostStatus.PUBLIC)) {
            return true;
        } else {
            if (username == null) {
                throw new AccessDeniedException("ERR_POST_ANONYMOUS");
            }
            BlogAuth blogAuth = blogAuthRepository.findByBlogNmAndMemberEmail(post.getBlog().getBlogNm(), username);
            if (post.getPostStatus().equals(PostStatus.PERMITTED)) {
                if (blogAuth.getRole().equals(Role.USER) || blogAuth.getRole().equals(Role.ADMIN)) {
                    return true;
                } else {
                    throw new AccessDeniedException("ERR_POST_PERMISSION_USER");
                }
            } else if (post.getPostStatus().equals(PostStatus.DELETE)) {
                throw new AccessDeniedException("ERR_POST_DELETED");
            } else {
                if (blogAuth.getRole().equals(Role.ADMIN)) {
                    return true;
                } else {
                    throw new AccessDeniedException("ERR_POST_PERMISSION_ADMIN");
                }
            }
        }
    }
    public boolean checkPostAdmin(Long postId, String username){
        if(username == null){
            throw new AccessDeniedException("ERR_POST_ANONYMOUS");
        }else{
            Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("ERR_POST_NUM"));
            BlogAuth blogAuth = blogAuthRepository.findByBlogNmAndMemberEmail(post.getBlog().getBlogNm(), username);
            if(blogAuth == null){
                throw new AccessDeniedException("ERR_POST_PERMISSION_ADMIN");
            }else {
                if (blogAuth.getRole().equals(Role.ADMIN)) {
                    return true;
                } else {
                    throw new AccessDeniedException("ERR_POST_PERMISSION_ADMIN");
                }
            }
        }
    }

    public Boolean checkCommentAuth(Long commentId, String username) throws NullPointerException, EntityNotFoundException, AccessDeniedException {
        if (username.isEmpty()) {
            return false;
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("ERR_COMMENT_NOT_FOUND"));
        if (comment.getMember().getEmail().equals(username) || comment.getPost().getBlog().getMember().getEmail().equals(username)) {
            return true;
        }
        return false;
    }
}
