package com.example.myblog.config;

import com.example.myblog.constant.AccessAuthType;
import com.example.myblog.entity.AccessAuth;
import com.example.myblog.entity.Topic;
import com.example.myblog.repository.AccessAuthRepository;
import com.example.myblog.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@ConditionalOnProperty(
            prefix = "spring.jpa.hibernate",
            name = "ddl-auto",
            havingValue = "create",
            matchIfMissing = false)
@Component
public class InitialDataSet implements CommandLineRunner {

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    AccessAuthRepository accessAuthRepository;

    @Override
    public void run(String... args) throws Exception {
        setTopic();
        setAccessAuth();
    }

    private void setTopic() {
        final List<Topic> topicList = Arrays.asList(
                new Topic("생활"),
                new Topic("여행"),
                new Topic("IT"),
                new Topic("시사"),
                new Topic("스포츠"),
                new Topic("취미")
        );
        topicRepository.saveAll(topicList);
    }

    private void setAccessAuth() {
        final List<AccessAuth> AccessAuthList = Arrays.asList(
                new AccessAuth("/members/myPage",               HttpMethod.GET,     AccessAuthType.AUTH_MEMBER),
                new AccessAuth("/members/myPage",               HttpMethod.POST,    AccessAuthType.AUTH_MEMBER),

                new AccessAuth("/blogs/new",                    HttpMethod.GET,     AccessAuthType.AUTH_MEMBER),
                new AccessAuth("/blogs/new",                    HttpMethod.POST,    AccessAuthType.AUTH_MEMBER),
                new AccessAuth("/blogs/myPage/",                HttpMethod.GET,     AccessAuthType.PER_OF_REQUESTER_BLOG),      //블로그 검색
                new AccessAuth("/blogs/myPage/",                HttpMethod.POST,    AccessAuthType.PER_OF_REQUESTER_BLOG),     //블로그 검색

                new AccessAuth("/posts/new/",                   HttpMethod.GET,     AccessAuthType.PER_OF_REQUESTER_POST),         //블로그 검색
                new AccessAuth("/posts/new/",                   HttpMethod.POST,    AccessAuthType.PER_OF_REQUESTER_POST),        //블로그 검색
                new AccessAuth("/posts/details/",               HttpMethod.GET,     AccessAuthType.PER_SET_ON_TARGET_POST),        //포스트 검색 후 블로그 검색
                new AccessAuth("/posts/details/",               HttpMethod.PATCH,   AccessAuthType.PER_OF_REQUESTER_POST),       //블로그 검색
                new AccessAuth("/posts/details/",               HttpMethod.DELETE,  AccessAuthType.PER_OF_REQUESTER_POST),      //블로그 검색

                new AccessAuth("/comments/new/",                HttpMethod.POST,    AccessAuthType.PER_SET_ON_TARGET_COMMENT),            //포스트 검색 후 블로그 검색
                new AccessAuth("/comments/details/",            HttpMethod.PATCH,   AccessAuthType.PER_OF_REQUESTER_COMMENT),       //포스트 검색 후 블로그 검색
                new AccessAuth("/comments/details/",            HttpMethod.DELETE,  AccessAuthType.PER_OF_REQUESTER_COMMENT),      //포스트 검색 후 블로그 검색

                new AccessAuth("/async/blogs/valid/",           HttpMethod.POST,    AccessAuthType.AUTH_MEMBER),
                new AccessAuth("/async/posts/upload/",          HttpMethod.POST,    AccessAuthType.PER_OF_REQUESTER_BLOG),               //블로그 검색
                new AccessAuth("/async/posts/preProcessing/",   HttpMethod.POST,    AccessAuthType.PER_OF_REQUESTER_BLOG),       //블로그 검색
                new AccessAuth("/async/category/new/",          HttpMethod.POST,    AccessAuthType.PER_OF_REQUESTER_BLOG)                //블로그 검색
        );
        accessAuthRepository.saveAll(AccessAuthList);
    }
}
