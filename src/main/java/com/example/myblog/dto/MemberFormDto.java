package com.example.myblog.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 6, max = 20, message = "비밀번호는 6자 이상, 20자 이하로 입력해주세요")
    private String password;

    @NotEmpty(message = "이름(닉네임)은 필수 입력 값입니다.")
    private String name;

    private String introduction;

}
