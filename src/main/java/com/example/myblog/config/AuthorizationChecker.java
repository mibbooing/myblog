package com.example.myblog.config;


import com.example.myblog.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthorizationChecker {

    public boolean checkAuth(HttpServletRequest request, Authentication authentication) {
        Object principalObj = authentication.getPrincipal();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (!(principalObj instanceof UserDetails) && request.getMethod().equals("GET")) {
            if(antPathMatcher.match("/posts/*/*", request.getRequestURI())){
                return true;
            }
            System.out.println("로그인되지 않은 사용자 접근");
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (antPathMatcher.match("/blogs/**", request.getRequestURI())) {
            /*블로그 접근 권한이 없다면 불가능*/
            System.out.println(getParamFromPath(request.getRequestURI()));
        }
        if (antPathMatcher.match("/posts/**", request.getRequestURI())) {
            /*블로그 접근 권한이 없다면 불가능*/

        }
        System.out.println(request.getRequestURI());
        return true;
    }

    private String getParamFromPath(String path){
        if(path.contains("/myPage/")){
            int idx = path.indexOf("/myPage/")+("/myPage/").length();
            path = path.substring(idx);
        }
        return path;
    }
}
