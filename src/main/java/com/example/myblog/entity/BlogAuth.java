package com.example.myblog.entity;

import com.example.myblog.constant.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="blog_auth")
@Getter
@ToString
public class BlogAuth extends BaseEntity{

    @Id
    @Column(name="blog_auth_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Role role;

    public BlogAuth() {}

    public BlogAuth(Blog blog, Member member, Role role) {
        this.blog = blog;
        this.member = member;
        this.role = role;
    }

    public void createBlogAuth(){

    }

}
