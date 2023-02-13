package com.example.myblog.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BlogMainFormDto {
    private Page<PostPreviewDto> postList;

    private List<CategoryDto> categoryDtoList = new ArrayList<>();

    private MemberInfoFormDto memberInfoFormDto;

    private BlogImgDto blogImgDto;

    private String blogNm;
}
