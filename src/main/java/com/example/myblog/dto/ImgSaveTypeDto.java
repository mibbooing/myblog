package com.example.myblog.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImgSaveTypeDto {
    private Long id;
    private String type;

    private String name;

    public ImgSaveTypeDto(Long id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }
}
