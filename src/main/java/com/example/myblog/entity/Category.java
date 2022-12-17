package com.example.myblog.entity;

import com.example.myblog.constant.CategoryType;
import com.example.myblog.dto.CategoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "parentCategory")
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String categoryNm;

    @ManyToOne()
    @JoinColumn(name="blog_id")
    private Blog blog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_category")
    private Category parentCategory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> children;

    @Enumerated(EnumType.STRING)
    private CategoryType depth;

    private int sortNum;

    public void createCategory(CategoryDto categoryDto, Blog blog, Category parentCategory){
        this.categoryNm = categoryDto.getCategoryNm();
        this.depth = categoryDto.getDepth();
        this.blog = blog;
        this.parentCategory = parentCategory;
        this.sortNum = categoryDto.getSortNum();
    }

    public void createDefaultCategory(String categoryNm, Blog blog, CategoryType depth){
        this.categoryNm = categoryNm;
        this.blog = blog;
        this.depth = depth;
    }

    public void updateCategory(CategoryDto categoryDto,Category parentCategory){
        this.categoryNm = categoryDto.getCategoryNm();
        this.depth = categoryDto.getDepth();
        this.sortNum = categoryDto.getSortNum();
        this.parentCategory = parentCategory;

    }

}
