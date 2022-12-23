package com.example.myblog.entity;

import com.example.myblog.constant.PostStatus;
import com.example.myblog.dto.PostDto;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "post")
@Getter
@ToString
public class Post extends BaseEntity {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @Lob
    private String contentUrl;

    private PostStatus postStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public void createPost(PostDto postDto, Blog blog, Category category) {
        this.title = postDto.getTitle();
        this.contentUrl = postDto.getContentUrl();
        this.postStatus = postDto.getPostStatus();
        this.blog = blog;
        this.category = category;
    }

    public void updatePost(PostDto postDto, Blog blog, Category category) {
        this.id = postDto.getId();
        this.title = postDto.getTitle();
        this.contentUrl = postDto.getContentUrl();
        this.postStatus = postDto.getPostStatus();
        this.blog = blog;
        this.category = category;
    }
}
