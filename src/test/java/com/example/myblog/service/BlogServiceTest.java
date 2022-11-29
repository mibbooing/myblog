package com.example.myblog.service;

import com.example.myblog.dto.BlogFormDto;
import com.example.myblog.dto.MemberFormDto;
import com.example.myblog.entity.Blog;
import com.example.myblog.entity.Member;
import com.example.myblog.repository.BlogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class BlogServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    BlogService blogService;

    @Autowired
    PasswordEncoder passwordEncoder;


    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setPassword("1234");
        memberFormDto.setIntroduction("반가워요!");
        return Member.createMember(memberFormDto, passwordEncoder);
    }


    @Test
    @DisplayName("블로그 생성 테스트")
    public void saveBlogTest() {
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        BlogFormDto blogFormDto = new BlogFormDto();
        blogFormDto.setBlogNm("testBlog");
        Blog savedBlog = blogService.saveBlog(blogFormDto, member.getEmail());

        assertEquals(blogFormDto.getBlogNm(), savedBlog.getBlogNm());
    }

    @Test
    @DisplayName("블로그 이름 중복생성 테스트")
    public void saveDuplicateBlogTest() {
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        BlogFormDto blogFormDto1 = new BlogFormDto();
        blogFormDto1.setBlogNm("testBlog");
        BlogFormDto blogFormDto2 = new BlogFormDto();
        blogFormDto2.setBlogNm("testBlog");
        Blog savedBlog = blogService.saveBlog(blogFormDto1, member.getEmail());



        Throwable e = assertThrows(IllegalStateException.class, ()->{
            blogService.saveBlog(blogFormDto2, member.getEmail());
        });

        assertEquals("이미 존재하는 블로그입니다.", e.getMessage());
    }
}