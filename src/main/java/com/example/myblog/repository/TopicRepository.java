package com.example.myblog.repository;

import com.example.myblog.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("select MIN(id) from Topic")
    public Long findOneDefault();
}
