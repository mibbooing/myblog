package com.example.myblog.entity;

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

}
