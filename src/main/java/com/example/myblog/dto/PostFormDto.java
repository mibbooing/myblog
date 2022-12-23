package com.example.myblog.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class PostFormDto {

    private String blogNm;

    private PostDto postDto;

    private List<PostImgDto> postImgDtoList = new ArrayList<>();

    private List<CategoryDto> categoryDtoList = new ArrayList<>();

}
