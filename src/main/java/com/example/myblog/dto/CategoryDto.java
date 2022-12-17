package com.example.myblog.dto;

import com.example.myblog.constant.CategoryType;
import com.example.myblog.constant.LogType;
import com.example.myblog.entity.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CategoryDto {

    private Long categoryId;

    private String categoryNm;

    private CategoryType depth;

    private Long blogId;

    private Long parentCategoryId;

    private int sortNum;

    private LogType reqType;

    private List<CategoryDto> categoryDtoList;

    public CategoryDto() {}

    public CategoryDto(Long categoryId, String categoryNm, CategoryType depth, Long blogId, Long parentCategoryId, int sortNum) {
        this.categoryId = categoryId;
        this.categoryNm = categoryNm;
        this.depth = depth;
        this.blogId = blogId;
        this.parentCategoryId = parentCategoryId;
        this.sortNum = sortNum;
    }
}
