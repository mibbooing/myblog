package com.example.myblog.entity;

import com.example.myblog.dto.PostImgDto;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "post_img")
@Getter
@ToString
public class PostImg {
    @Id
    @Column(name = "post_img_id")
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void createPostImg(PostImgDto postImgDto, Post post) {
        this.imgName = postImgDto.getImgName();
        this.oriImgName = postImgDto.getOriImgName();
        this.imgUrl = postImgDto.getImgUrl();
        this.repimgYn = postImgDto.getRepimgYn();
        this.post = post;
    }

    public void updatePostImg(PostImgDto postImgDto, Post post) {
        this.id = postImgDto.getId();
        this.imgName = postImgDto.getImgName();
        this.oriImgName = postImgDto.getOriImgName();
        this.imgUrl = postImgDto.getImgUrl();
        this.repimgYn = postImgDto.getRepimgYn();
        this.post = post;
    }
}
