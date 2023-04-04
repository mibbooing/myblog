package com.example.myblog.repository;

import com.example.myblog.constant.AccessAuthType;
import com.example.myblog.entity.AccessAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpMethod;

public interface AccessAuthRepository extends JpaRepository<AccessAuth, Long> {

    @Query(value = "select * from access_auth where :url REGEXP url and access_auth.method = :#{#method.name()}",nativeQuery = true)
    AccessAuth findByUrlAndMethod(String url, @Param("method") HttpMethod method);
}
