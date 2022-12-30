package com.example.myblog.repository;

import com.example.myblog.entity.BlogAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogAuthRepository extends JpaRepository<BlogAuth, Long> {

//    public BlogAuth findByBlogId(Long blogId);

}
