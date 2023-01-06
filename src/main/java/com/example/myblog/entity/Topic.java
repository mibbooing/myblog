package com.example.myblog.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "topic")
public class Topic {
    @Id
    @Column(name = "topic_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String topicName;

    public void createTopic(Long id, String topicName) {
        this.id = id;
        this.topicName = topicName;
    }

    public Topic() {
    }

    public Topic(String topicName) {
        this.topicName = topicName;
    }
}
