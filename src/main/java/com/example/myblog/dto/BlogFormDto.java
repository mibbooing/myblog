package com.example.myblog.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BlogFormDto {
    @NotBlank(message = "블로그이름은 필수 입력 값입니다.")
    @Length(min = 6, max = 20, message = "블로그 이름은 6자 이상, 20자 이하로 입력해주세요")
    private String blogNm;
}
