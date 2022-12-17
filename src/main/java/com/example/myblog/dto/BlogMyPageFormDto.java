package com.example.myblog.dto;

import com.example.myblog.constant.LogType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BlogMyPageFormDto {

    private BlogInfoFormDto blogInfoFormDto;

    private List<CategoryDto> categoryDtoList;

    private Map<String, LogType> logType;

    public BlogMyPageFormDto(BlogInfoFormDto blogInfoFormDto, List<CategoryDto> categoryDtoList, Map<String, LogType> logType) {
        this.blogInfoFormDto = blogInfoFormDto;
        this.categoryDtoList = categoryDtoList;
        this.logType = logType;
    }

}
