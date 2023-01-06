package com.example.myblog.entity;

import com.example.myblog.constant.AccessAuthType;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpMethod;

import javax.persistence.*;

@Entity
@Table(name="access_auth")
@Getter
@ToString
public class AccessAuth {

    @Id
    @Column(name="access_auth_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accessAuthId;

    private String url;

    @Enumerated(EnumType.STRING)
    private HttpMethod method;

    @Enumerated(EnumType.STRING)
    private AccessAuthType inspectionType;

    public AccessAuth() {}

    public AccessAuth(String url, HttpMethod method, AccessAuthType inspectionType) {
        this.url = url;
        this.method = method;
        this.inspectionType = inspectionType;
    }
}
