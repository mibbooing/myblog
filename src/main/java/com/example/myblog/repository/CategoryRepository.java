package com.example.myblog.repository;

import com.example.myblog.constant.CategoryType;
import com.example.myblog.constant.LogType;
import com.example.myblog.dto.CategoryDto;
import com.example.myblog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Category findByBlogIdAndDepth(Long blogId, CategoryType depth);

    @Query("select new com.example.myblog.dto.CategoryDto(c.id, c.categoryNm, c.depth, c.blog.id, c.parentCategory.id, c.sortNum ) " +
            "from Category c join c.blog b " +
            "where b.id = c.blog.id " +
            "and c.blog.id = :blogId " +
            "order by c.depth asc, c.parentCategory.id asc, c.sortNum asc"
    )
    public List<CategoryDto> findByBlogId(Long blogId);
}
