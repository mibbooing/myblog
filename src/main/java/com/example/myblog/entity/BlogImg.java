package com.example.myblog.entity;


import com.example.myblog.dto.ImgDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "blog_img")
@Getter
@Setter
public class BlogImg extends BaseEntity {
    @Id
    @Column(name = "blog_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    public BlogImg() {
    }

    public BlogImg(ImgDto imgDto, Blog blog) {
        this.imgUrl = imgDto.getImgUrl();
        this.imgName = imgDto.getImgName();
        this.oriImgName = imgDto.getOriImgName();
        this.repimgYn = imgDto.getRepimgYn();
        this.blog = blog;
    }

    public void initBlogImg(Blog blog, String repimgYn) {
        this.blog = blog;
        this.repimgYn = repimgYn;
    }

    public void updateBlogImg(ImgDto imgDto, Blog blog) {
        this.id = imgDto.getId();
        this.imgUrl = imgDto.getImgUrl();
        this.imgName = imgDto.getImgName();
        this.oriImgName = imgDto.getOriImgName();
        this.repimgYn = imgDto.getRepimgYn();
        this.blog = blog;
    }

}
