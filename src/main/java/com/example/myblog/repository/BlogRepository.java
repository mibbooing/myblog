package com.example.myblog.repository;

import com.example.myblog.dto.BlogFormDto;
import com.example.myblog.dto.BlogInfoFormDto;
import com.example.myblog.dto.BlogListDto;
import com.example.myblog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    Blog findById(String id);
    Blog findByBlogNm(String blogNm);

    @Query("select new com.example.myblog.dto.BlogListDto(b.id, b.blogNm, i.imgUrl) "+
            " from Blog b join b.member m " +
            " on b.member.id = :memberId and m.id = b.member.id " +
            " left join BlogImg i " +
            " on b.id = i.blog.id "
    )
    List<BlogListDto> findAllBlogDtoList(Long memberId);

    @Query("select new com.example.myblog.dto.BlogInfoFormDto(b.id, i.id, b.blogNm, b.topic.id, i.imgName, i.oriImgName, i.imgUrl, i.repimgYn) " +
            "from BlogImg i left join i.blog b " +
            "on b.id = i.blog.id " +
            "and i.repimgYn = :repimgYn " +
            "and b.blogNm = :blogNm"
    )
    BlogInfoFormDto findByBlogNmAndRepImgYn(String blogNm, String repimgYn);

}
