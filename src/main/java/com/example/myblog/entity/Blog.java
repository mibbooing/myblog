package com.example.myblog.entity;

import com.example.myblog.dto.BlogFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="blog")
@Getter
@Setter
@ToString
public class Blog {
    @Id
    @Column(name = "blog_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String blogNm;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="topic_id")
    private Topic topic;

    public static Blog createBlog(BlogFormDto blogFormDto, Member member, Topic topic){
        Blog blog = new Blog();
        blog.setBlogNm(blogFormDto.getBlogNm());
        blog.setMember(member);
        blog.setTopic(topic);
        return blog;
    }

    public void updateBlog(String blogNm, Topic topic){
        this.setBlogNm(blogNm);
        this.setTopic(topic);
    }

}
