package com.example.myblog.repository;

import com.example.myblog.dto.MemberInfoFormDto;
import com.example.myblog.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    @Query("select new com.example.myblog.dto.MemberInfoFormDto(m.id, m.email, m.name, m.introduction, img.imgName, img.oriImgName, img.imgUrl, img.repimgYn) " +
            "from MemberImg img right join img.member m " +
            "on img.member.id = m.id " +
            "and img.repimgYn = 'Y'" +
            "where m.id = :id "
    )
    MemberInfoFormDto findByIdForBlogMain(Long id);

}
