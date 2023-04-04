package com.example.myblog.repository;

import com.example.myblog.entity.BlogAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BlogAuthRepository extends JpaRepository<BlogAuth, Long> {

    BlogAuth findByBlogId(Long blogId);

    BlogAuth findByBlogIdAndMemberId(Long blogId, Long MemberId);

    @Query("select ba from BlogAuth ba where ba.blog.id = (select b.id from Blog b where b.blogNm = :blogNm) " +
            "and ba.member.id = (select m.id from Member m where m.email = :email)"
    )
    BlogAuth findByBlogNmAndMemberEmail(String blogNm, String email);
}
