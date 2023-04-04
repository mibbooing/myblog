package com.example.myblog;

import com.example.myblog.constant.AccessAuthType;
import com.example.myblog.entity.AccessAuth;
import com.example.myblog.repository.AccessAuthRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccessAuthTest {

    @Autowired
    AccessAuthRepository accessAuthRepository;

    @Test
    @DisplayName("AccessAuth 테스트1")
    public void accessAuthTest(){
        AccessAuth accessAuth=accessAuthRepository.findByUrlAndMethod("/blogs/myPage/test12", HttpMethod.GET);
//        assertEquals(accessAuth.getInspectionType(), AccessAuthType.PER_OF_REQUESTER);
    }
}
