package com.example.myblog.config;


import com.example.myblog.constant.PostStatus;
import com.example.myblog.entity.AccessAuth;
import com.example.myblog.entity.Post;
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

@Component
@RequiredArgsConstructor
@Transactional
public class AuthorizationChecker_backup {
    /*
     * 추가될 기능에 대한(COMMENT, ??) 권한 인가 로직 작성 필요!
     * */

    private final MemberRepository memberRepository;
    private final BlogRepository blogRepository;
    private final PostRepository postRepository;
    private final BlogAuthRepository blogAuthRepository;
    private final AccessAuthRepository accessAuthRepository;

    public boolean checkAuth(HttpServletRequest request, Authentication authentication) {
        Object principalObj = authentication.getPrincipal();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (!(principalObj instanceof UserDetails) && request.getMethod().equals("GET")) {
            if (antPathMatcher.match("/posts/details/*", request.getRequestURI()) && HttpMethod.valueOf(request.getMethod()).equals(HttpMethod.GET)) {
                System.out.println("일치함");
                try {
                    AccessAuth accessAuth = accessAuthRepository.findByUrlAndMethod(request.getRequestURI(), HttpMethod.valueOf(request.getMethod()));
                    Post post = postRepository.findById(getTargetId(accessAuth, request.getRequestURI())).orElseThrow(EntityNotFoundException::new);
                    if (post == null) return false;
                    else if (post.getPostStatus().equals(PostStatus.PUBLIC)) {
                        return true;
                    }
                } catch (Exception e){
                    new CustomAccessDeniedHandler();
                    return false;
                }
            }
            System.out.println("로그인되지 않은 사용자 접근");      //그 외 인증되지 않은 접근 거부
        }
        return true;
    }

    public boolean CheckPermission(Post post, AccessAuth accessAuth){
        return true;
    }

    public Long getTargetId(AccessAuth accessAuth, String target) {
        return Long.parseLong(extractionVariable(accessAuth, target));
    }

    public String getTargetName(AccessAuth accessAuth, String target) {
        return extractionVariable(accessAuth, target);
    }

    public String extractionVariable(AccessAuth accessAuth, String target) {
        String resultVariable = target.substring(accessAuth.getUrl().length());
        if (resultVariable.contains("/")) {
            System.out.println(resultVariable);
            resultVariable.substring(0, resultVariable.indexOf("/"));
        }
        return resultVariable;
    }


//        System.out.println("reqUrl: "+request.getRequestURI());
//        Object principalObj = authentication.getPrincipal();
//        AntPathMatcher antPathMatcher = new AntPathMatcher();
//        if (!(principalObj instanceof UserDetails) && request.getMethod().equals("GET")) {
//            if(antPathMatcher.match("/posts/*/*", request.getRequestURI())){
//                Post post = postRepository.findById(Long.parseLong(getParamFromPath(request))).orElseThrow(EntityNotFoundException::new);
//                if(post.getPostStatus().equals(PostStatus.PUBLIC)){
//                    return true;    //메소드가 GET이고 게시물 상태가 PUBLIC 게시물에 접근하는 경우 접근허용 (인증여부 확인없음)
//                }
//            }
//            System.out.println("로그인되지 않은 사용자 접근");      //그 외 인증되지 않은 접근 거부
//            return false;
//        }
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Member member = memberRepository.findByEmail(userDetails.getUsername());    // 요청 사용자 정보 조회 후 member변수에 할당
//        Blog blog;
//        if (antPathMatcher.match("/blogs/**", request.getRequestURI()) || antPathMatcher.match("/posts/new/*", request.getRequestURI()) || antPathMatcher.match("/posts/upload/*", request.getRequestURI())) {
//            /*블로그 접근 권한이 없다면 불가능*/
//            blog = blogRepository.findByBlogNm(getParamFromPath(request));
//            return blogAuthCheck(member, blog, null);       //해당 블로그의 myPage 접근권한 확인
//        } else if (antPathMatcher.match("/posts/**", request.getRequestURI())) {
//            /*게시글 접근 권한이 없다면 불가능*/
//            Post post = postRepository.findById(Long.parseLong(getParamFromPath(request))).orElseThrow(EntityNotFoundException::new);
//            blog = post.getBlog();
//            return blogAuthCheck(member, blog, post);       //해당 게시물 접근권한 확인
//        } else if (antPathMatcher.match("/async/**",request.getRequestURI())){
//
//        }
//        return true;
//    }
//
//    private String getParamFromPath(HttpServletRequest request){
//        String path = request.getRequestURI();//HTTP REQUEST 의 URI 정보에서 blogAuth 조회에 필요한 변수 추출 (blogNm, postId)
//        String result = new String();
//        if(path.contains("/myPage/")){
//            int idx = path.indexOf("/myPage/")+("/myPage/").length();   // 블로그 MYPAGE
//            result = path.substring(idx);
//        }else if(path.contains("/posts/")){      //게시물 조회,수정,삭제
//            int idx;
//            if(path.contains("/new/")){
//                idx = path.indexOf("/posts/new/")+("/posts/new/").length();     //신규 게시물 작성 Form 요청 또는 작성정보 Submit
//            } else if(path.contains("/upload/")){
//                idx = path.indexOf("/posts/upload/")+("/posts/upload/").length();
//            } else{
//                idx = path.indexOf("/posts/")+("/posts/").length();
//            }
//            result = path.substring(idx);
//            return result;
//        }else if(path.contains("/category/new/")){
//            result = request.getParameter("blogNm");
//            System.out.println("request:" + request);
//        }
//        return result;
//    }
//
//    private boolean blogAuthCheck(Member member, Blog blog, Post post){
//        if(member == null || blog == null){
//            return false;
//        }
//        BlogAuth blogAuth = blogAuthRepository.findByBlogIdAndMemberId(blog.getId(), member.getId());
//        if(blogAuth != null) {
//            if(post == null) {      // 블로그에 대한 권한 확인
//                if (blogAuth.getRole().equals(Role.ADMIN)) {
//                    return true;        // 해당 블로그에 대한 ADMIN 권한(블로그 생성시 사용자에게 부여)을 가지고 있다면 접근 인가
//                }
//            }else{                  //게시물에 대한 권한 확인
//                return checkPostStatus(post.getPostStatus(), blogAuth);         //해당 게시물에 대한 권한 확인 method 호출
//            }
//        }
//        return false;
//    }
//
//    private boolean checkPostStatus(PostStatus postStatus, BlogAuth blogAuth){
//        switch (postStatus){
//            case PERMITTED:                 //허가된 사용자(블로그 이웃등록)일 경우
//                if(blogAuth.getRole().equals(Role.ADMIN) || blogAuth.getRole().equals(Role.USER)){
//                    return true;
//                }
//                break;
//            case HIDE: case TEMP:           //해당 블로그에 대한 ADMIN 사용자인 경우
//                if(blogAuth.getRole().equals(Role.ADMIN)){
//                    return true;
//                }
//                break;
//            default:                        //그 외 상태일 경우 모두 접근 거부(추후 프로그램 관리자(SUPER ADMIN) 권한 추가예정)
//                break;
//        }
//        return false;
//    }
}
