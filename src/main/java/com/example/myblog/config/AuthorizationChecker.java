package com.example.myblog.config;


import com.example.myblog.constant.AccessAuthType;
import com.example.myblog.constant.PostStatus;
import com.example.myblog.constant.Role;
import com.example.myblog.entity.*;
import com.example.myblog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class AuthorizationChecker {
    /*
     * 추가될 기능에 대한(COMMENT, ??) 권한 인가 로직 작성 필요!
     * */

    private final MemberRepository memberRepository;
    private final BlogRepository blogRepository;
    private final PostRepository postRepository;
    private final BlogAuthRepository blogAuthRepository;
    private final AccessAuthRepository accessAuthRepository;

    public boolean checkBlogAuth(String blogNm, String username) {
        try {
            BlogAuth blogAuth = blogAuthRepository.findByBlogNmAndMemberEmail(blogNm, username);
            if (blogAuth.getRole().equals(Role.ADMIN)) {
                return true;
            }
        } catch (Exception e) {
            if(e instanceof NullPointerException){
                System.out.println("검색실패");
                throw new NullPointerException("권한검색실패");
            }
        }
        return false;
    }
}
