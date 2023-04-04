package com.example.myblog.dto;

import com.example.myblog.entity.Topic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class BlogFormDto {
    @NotBlank(message = "블로그이름은 필수 입력 값입니다.")
    @Length(min = 3, max = 20, message = "블로그 이름은 3자 이상, 20자 이하로 입력해주세요")
    private String blogNm;

    @NotNull(message = "블로그주제는 필수 선택 값입니다.")
    private Long topicId;

    private List<Topic> topicList = new ArrayList<>();
}
