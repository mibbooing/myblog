package com.example.myblog.repository;

import com.example.myblog.dto.PostPreviewDto;
import com.example.myblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

//    @Query("select new com.example.myblog.dto.CategoryDto(c.id, c.categoryNm, c.depth, c.blog.id, c.parentCategory.id, c.sortNum ) " +
//                "from Category c join c.blog b " +
//                "where b.id = c.blog.id " +
//                "and c.blog.id = :blogId " +
//                "order by c.depth asc, c.parentCategory.id asc, c.sortNum asc"
//    )
//    List<PostPreviewDto> findByBlogNm(String blogNm);
}
