package com.example.myblog.repository;

import com.example.myblog.dto.CommentDto;
import com.example.myblog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select new com.example.myblog.dto.CommentDto(" +
            "comm.id, " +
            "case" +
            "   when comm.commentStatus = com.example.myblog.constant.PostStatus.PUBLIC then comm.content " +
            "   when comm.commentStatus = com.example.myblog.constant.PostStatus.HIDE and (comm.member.id = :memberId or p.blog.member.id = :postId) then comm.content" +
            "   else null " +
            "end," +
            "comm.member.id," +
            "comm.member.email, " +
            "comm.member.name, " +
            "p.id, " +
            "comm.commentStatus, " +
            "comm.commentStatusRequester) " +
            "from Comment comm left join comm.post p " +
            "on comm.post.id = p.id " +
            "where p.id = :postId")
    Page<CommentDto> findByPostIdAndMemberId(Pageable pageable, Long postId, Long memberId);

    @Query("select new com.example.myblog.dto.CommentDto(" +
                "comm.id, " +
                "case" +
                "   when comm.commentStatus = com.example.myblog.constant.PostStatus.PUBLIC then comm.content " +
                "   else null " +
                "end," +
                "comm.member.id," +
                "comm.member.email," +
                "comm.member.name, " +
                "p.id, " +
                "comm.commentStatus, " +
                "comm.commentStatusRequester) " +
                "from Comment comm left join comm.post p " +
                "on comm.post.id = p.id " +
                "where p.id = :postId")
    Page<CommentDto> findByPostId(Pageable pageable, Long postId);

}
