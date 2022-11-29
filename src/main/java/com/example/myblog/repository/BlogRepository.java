package com.example.myblog.repository;

import com.example.myblog.dto.BlogFormDto;
import com.example.myblog.dto.BlogListDto;
import com.example.myblog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Blog findByBlogNm(String blogNm);

    @Query("select new com.example.myblog.dto.BlogListDto(b.id, b.blogNm, m.id) "+
            "from Blog b join b.member m  " +
            "where b.member.id = :memberId and b.member.id = m.id"
    )
    List<BlogListDto> findAllBlogDtoList(Long memberId);

}
