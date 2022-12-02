package com.example.myblog.dto;


import com.example.myblog.constant.LogType;
import com.example.myblog.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLogDto {

    private String member_name_log;

    private String member_introduction_log;

    private String member_profile_img_log;

    private LogType logType;

    public MemberLogDto(String member_name_log, String member_introduction_log, String member_profile_img_log, LogType logType){
        this.member_name_log = member_name_log;
        this.member_introduction_log = member_introduction_log;
        this.member_profile_img_log = member_profile_img_log;
        this.logType = logType;
    }
}
