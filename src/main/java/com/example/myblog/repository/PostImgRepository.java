package com.example.myblog.repository;

import com.example.myblog.dto.PostImgDto;
import com.example.myblog.entity.PostImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostImgRepository extends JpaRepository<PostImg, Long> {

    @Query("select new com.example.myblog.dto.PostImgDto(img.id, p.id, img.imgName, img.oriImgName, img.imgUrl, img.repimgYn) " +
            "from PostImg img join img.post p " +
            "where p.id = img.post.id " +
            "and img.repimgYn = 'Y' " +
            "and p.id = :postId")
    PostImgDto findByPostId(Long postId);
}
