package com.example.myblog.repository;

import com.example.myblog.dto.BlogImgDto;
import com.example.myblog.entity.BlogImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BlogImgRepository extends JpaRepository<BlogImg, Long> {

    @Query("select new com.example.myblog.dto.BlogImgDto(img.id, img.imgName, img.oriImgName, img.imgUrl, img.repimgYn) " +
            "from BlogImg img join img.blog b " +
            "where img.blog.id = :blogId " +
            "and img.repimgYn = :repimgYn " +
            "and img.blog.id = b.id "
    )
    BlogImgDto findByBlogIdAndRepimgYn(Long blogId, String repimgYn);
}
