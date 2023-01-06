package com.example.myblog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BlogMainFormDto {
    private List<PostPreviewDto> postList = new ArrayList<>();

    private List<CategoryDto> categoryList = new ArrayList<>();

    private MemberInfoFormDto memberInfoFormDto;
}
