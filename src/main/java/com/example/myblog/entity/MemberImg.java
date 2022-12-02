package com.example.myblog.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "member_img")
@Getter
@Setter
public class MemberImg {
    @Id
    @Column(name = "member_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void createMemberImg(String imgName, String oriImgName, String imgUrl, Member member, String repimgYn){
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.member = member;
        this.repimgYn = repimgYn;
    }

    public void updateMemberImg(String imgName, String oriImgName, String imgUrl){
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
}
