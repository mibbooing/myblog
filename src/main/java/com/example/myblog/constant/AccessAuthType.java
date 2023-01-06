package com.example.myblog.constant;

public enum AccessAuthType {       //접근 권한별 검사해야할 항목 구분
    AUTH_MEMBER,                     //회원 인증여부 확인
    PER_OF_REQUESTER_BLOG,           //요청자의 권한 확인(타겟(BLOG)에 대한 ADMIN권한)              blogNm
    PER_OF_REQUESTER_POST,           //요청자의 권한 확인(타겟(POST)에 대한 ADMIN권한)              postId
    PER_OF_REQUESTER_COMMENT,        //요청자의 권한 확인(타겟(COMMENT)에 대한 ADMIN권한)           postId
    PER_SET_ON_TARGET_BLOG,          //요청자의 권한과 요청한 타겟에 설정된 권한(블로그 접근 권한) 확인. blogNm
    PER_SET_ON_TARGET_POST,          //요청자의 권한과 요청한 타겟에 설정된 권한(블로그 접근 권한) 확인. postId
    PER_SET_ON_TARGET_COMMENT,       //요청자의 권한과 요청한 타겟에 설정된 권한(블로그 접근 권한) 확인. commentId
}
