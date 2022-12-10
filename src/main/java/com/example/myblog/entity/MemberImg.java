package com.example.myblog.entity;

import com.example.myblog.dto.ImgDto;
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

    public MemberImg() {
    }

    public MemberImg(ImgDto imgDto, Member member) {
        this.imgUrl = imgDto.getImgUrl();
        this.imgName = imgDto.getImgName();
        this.oriImgName = imgDto.getOriImgName();
        this.repimgYn = imgDto.getRepimgYn();
        this.member = member;
    }

    public void updateMemberImg(ImgDto imgDto, Member member){
        this.id = imgDto.getId();
        this.imgUrl = imgDto.getImgUrl();
        this.imgName = imgDto.getImgName();
        this.oriImgName = imgDto.getOriImgName();
        this.repimgYn = imgDto.getRepimgYn();
        this.member = member;
    }
}
