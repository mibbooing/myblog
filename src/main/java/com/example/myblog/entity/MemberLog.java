package com.example.myblog.entity;

import com.example.myblog.constant.LogType;
import com.example.myblog.dto.MemberLogDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="memberLog")
@Getter
@Setter
@ToString
public class MemberLog extends BaseEntity{
    @Id
    @Column(name = "member_log_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    private String member_name_log;

    private String member_introduction_log;

    private String member_profile_img_log;

    @Enumerated(EnumType.STRING)
    private LogType logType;

    public static MemberLog createMemberLog(Member member, MemberLogDto memberLogDto){
        MemberLog memberLog = new MemberLog();
        memberLog.setMember(member);
        memberLog.setMember_name_log(member.getName());
        memberLog.setMember_introduction_log(memberLogDto.getMember_introduction_log());
        memberLog.setMember_profile_img_log(memberLogDto.getMember_profile_img_log());
        memberLog.setLogType(memberLogDto.getLogType());
        return memberLog;
    }
}
