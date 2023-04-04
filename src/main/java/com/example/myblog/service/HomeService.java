package com.example.myblog.service;

import com.example.myblog.dto.HomePostPreviewDto;
import com.example.myblog.entity.Topic;
import com.example.myblog.repository.BlogRepository;
import com.example.myblog.repository.PostRepository;
import com.example.myblog.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HomeService {

    private final BlogRepository blogRepository;

    private final PostRepository postRepository;

    private final TopicRepository topicRepository;

    public Page<HomePostPreviewDto> getHomeInfo(int page, int size, Long topicId){
        Pageable pageable = PageRequest.of(page, size);
        Page<HomePostPreviewDto> homePostPreviewDtoList  = postRepository.findByTopicId(pageable, topicId);
        return homePostPreviewDtoList;
    }

    public List<Topic> getTopicList() {
        return topicRepository.findAll();
    }

    public Long getDefaultTopicId(){
        return topicRepository.findOneDefault();
    }
}
