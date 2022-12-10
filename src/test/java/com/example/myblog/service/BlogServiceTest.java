package com.example.myblog.service;

import com.example.myblog.dto.*;
import com.example.myblog.entity.Blog;
import com.example.myblog.entity.BlogImg;
import com.example.myblog.entity.Member;
import com.example.myblog.entity.Topic;
import com.example.myblog.repository.BlogImgRepository;
import com.example.myblog.repository.BlogRepository;
import com.example.myblog.repository.MemberImgRepository;
import com.example.myblog.repository.TopicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    MemberImgRepository memberImgRepository;

    @Autowired
    BlogImgRepository blogImgRepository;

    @Autowired
    TopicRepository topicRepository;


    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setPassword("1234");
        memberFormDto.setIntroduction("반가워요!");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    public List<Topic> createTopic() {
        List<Topic> topicList = new ArrayList<>();
        Topic topic1 = new Topic();
        topic1.createTopic(1L, "시사");
        Topic topic2 = new Topic();
        topic2.createTopic(2L, "생활");
        Topic topic3 = new Topic();
        topic3.createTopic(3L, "여행");
        Topic topic4 = new Topic();
        topic4.createTopic(4L, "IT");
        Topic topic5 = new Topic();
        topic5.createTopic(5L, "스포츠");


        topicList.add(topic1);
        topicList.add(topic2);
        topicList.add(topic3);
        topicList.add(topic4);
        topicList.add(topic5);

        return topicList;
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


        Throwable e = assertThrows(IllegalStateException.class, () -> {
            blogService.saveBlog(blogFormDto2, member.getEmail());
        });

        assertEquals("이미 존재하는 블로그입니다.", e.getMessage());
    }

    @Test
    @DisplayName("블로그 생성, 이미지 업로드 테스트")
    public void updateBlogInfoTest() throws Exception {
        List<Topic> topicList = createTopic();
        for (Topic topic : topicList) {
            topicRepository.save(topic);
            System.out.println(topic.getId());
        }

        Member member = createMember();
        Member savedMember = memberService.saveMember(member);
        BlogFormDto blogFormDto = new BlogFormDto();
        blogFormDto.setBlogNm("블로그만들기테스트");
        blogFormDto.setTopicId(3L);
        Blog blog = blogService.saveBlog(blogFormDto, savedMember.getEmail());

        System.out.println("블로그 ID : " + blog.getId());


        BlogInfoFormDto blogInfoFormDto = new BlogInfoFormDto();
        MockMultipartFile multipartFile = new MockMultipartFile("C:/myblog/blog/", "image.jpg", "image/jpg", new byte[]{1, 2, 3, 4});
        blogInfoFormDto.setBlogNm("블로그만들어버리기");
        blogInfoFormDto.setTopicId(2L);
        blogInfoFormDto.setBlogId(blog.getId());
        blogService.updateBlogInfo(blogInfoFormDto, multipartFile);
        BlogImgDto bto = blogImgRepository.findByBlogIdAndRepimgYn(blog.getId(), "Y");

        assertEquals(bto.getOriImgName(), multipartFile.getOriginalFilename());

    }

    @Test
    @DisplayName("블로그목록조회 테스트")
    public void getMyBlogListTest() {
        List<Topic> topicList = createTopic();
        for (Topic topic : topicList) {
            topicRepository.save(topic);
            System.out.println(topic.getId());
        }

        Member member = createMember();
        Member savedMember = memberService.saveMember(member);
        BlogFormDto blogFormDto = new BlogFormDto();
        blogFormDto.setBlogNm("블로그만들기테스트");
        blogFormDto.setTopicId(3L);
        Blog blog = blogService.saveBlog(blogFormDto, savedMember.getEmail());

        BlogFormDto blogFormDto1 = new BlogFormDto();
        blogFormDto1.setBlogNm("블로그만들기테스트12");
        blogFormDto1.setTopicId(3L);
        Blog blog1 = blogService.saveBlog(blogFormDto1, savedMember.getEmail());

        List<BlogListDto> blogList = new ArrayList<>();
        blogList = blogService.getMyBlogList(member.getEmail());

        for (BlogListDto list : blogList) {
            System.out.println(list.getId());
            System.out.println(list.getBlogNm());
            System.out.println(list.getImgUrl());
        }

    }

    @Test
    @DisplayName("내 블로그 마이페이지 조회 테스트")
    public void getMyBlogInfoTest() throws Exception {
        List<Topic> topicList = createTopic();
        for (Topic topic : topicList) {
            topicRepository.save(topic);
            System.out.println(topic.getId());
        }

        Member member = createMember();
        Member savedMember = memberService.saveMember(member);
        BlogFormDto blogFormDto = new BlogFormDto();
        blogFormDto.setBlogNm("블로그만들기테스트");
        blogFormDto.setTopicId(3L);
        Blog blog = blogService.saveBlog(blogFormDto, savedMember.getEmail());

        System.out.println("블로그 ID : " + blog.getId());


        BlogInfoFormDto blogInfoFormDto = new BlogInfoFormDto();
        MockMultipartFile multipartFile = new MockMultipartFile("C:/myblog/blog/", "image.jpg", "image/jpg", new byte[]{1, 2, 3, 4});

        blogInfoFormDto.setBlogNm("블로그만들어버리기");
        blogInfoFormDto.setTopicId(2L);
        blogInfoFormDto.setBlogId(blog.getId());
        blogService.updateBlogInfo(blogInfoFormDto, multipartFile);
        BlogImgDto bto = blogImgRepository.findByBlogIdAndRepimgYn(blog.getId(), "Y");

        BlogInfoFormDto getBlogInfoFormDto = blogService.getMyBlogForm("블로그만들어버리기");

        assertEquals(blogInfoFormDto.getBlogId(), getBlogInfoFormDto.getBlogId());

    }
}