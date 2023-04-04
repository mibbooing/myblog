package com.example.myblog.repository;

import com.example.myblog.dto.MemberImgDto;
import com.example.myblog.dto.MemberInfoFormDto;
import com.example.myblog.entity.MemberImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {

    @Query("select new com.example.myblog.dto.MemberInfoFormDto(img.id, m.email, m.name, m.introduction, img.imgName, img.oriImgName, img.imgUrl, img.repimgYn) " +
            "from MemberImg img right join img.member m " +
            "on img.member.id = m.id " +
            "and img.repimgYn = :repimgYn " +
            "where img.member.id = :memberId"
    )
    MemberInfoFormDto findByMemberIdAndRepimgYn(Long memberId, String repimgYn);
}
