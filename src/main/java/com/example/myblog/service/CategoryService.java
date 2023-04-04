package com.example.myblog.service;

import com.example.myblog.constant.CategoryType;
import com.example.myblog.constant.LogType;
import com.example.myblog.dto.CategoryDto;
import com.example.myblog.entity.Blog;
import com.example.myblog.entity.Category;
import com.example.myblog.repository.BlogRepository;
import com.example.myblog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final BlogRepository blogRepository;

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategory(String blogNm) {
        Blog blog = blogRepository.findByBlogNm(blogNm);
        List<CategoryDto> categoryList = categoryRepository.findByBlogId(blog.getId());
        return categoryList;
    }

    public void saveCategory(String blogNm, List<CategoryDto> categoryDtoList) {
        Blog blog = blogRepository.findByBlogNm(blogNm);
        Category category;
        Category parentCategory;
        Category mainCategory;
        if (categoryDtoList != null) {
            for (CategoryDto categoryDto : categoryDtoList) {
                if (categoryDto.getCategoryId() == null) {
                    category = new Category();
                    parentCategory = categoryRepository.findById(categoryDto.getParentCategoryId()).orElseThrow(EntityExistsException::new);
                    category.createCategory(categoryDto, blog, parentCategory);
                    categoryRepository.save(category);
                } else if (categoryDto.getCategoryId() != null) {
                    if (categoryDto.getReqType() == LogType.UPDATE) {
                        category = categoryRepository.findById(categoryDto.getCategoryId()).orElseThrow(EntityExistsException::new);
                        if (categoryDto.getDepth() == CategoryType.DEFAULT) {
                            category.updateCategory(categoryDto, null);
                        } else {
                            parentCategory = categoryRepository.findById(categoryDto.getParentCategoryId()).orElseThrow(EntityExistsException::new);
                            category.updateCategory(categoryDto, parentCategory);
                        }
                    } else if (categoryDto.getReqType() == LogType.DELETE) {
                        try {
                            category = categoryRepository.findById(categoryDto.getCategoryId()).orElseThrow(EntityExistsException::new);
                        } catch (EntityExistsException e) {
                            continue;
                        }
                        categoryRepository.delete(category);
                    }
                }
            }
        } else {
            category = new Category();
            category.createDefaultCategory(blog.getBlogNm(), blog, CategoryType.DEFAULT);
            categoryRepository.save(category);
        }
    }

    public Category createMainCategory(String blogNm) {
        Blog blog = blogRepository.findByBlogNm(blogNm);
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDepth(CategoryType.MAIN);
        category.createCategory(categoryDto, blog, categoryRepository.findByBlogIdAndDepth(blog.getId(), CategoryType.DEFAULT));
        return categoryRepository.save(category);
    }
}
