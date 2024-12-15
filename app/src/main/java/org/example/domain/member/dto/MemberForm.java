package org.example.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MemberForm {

    @NotBlank(message = "이메일은 반드시 입력해야 합니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "사용자 이름은 반드시 입력해야 합니다.")
    @Size(max = 20, message = "사용자 이름은 최대 20글자까지 가능합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 반드시 입력해야 합니다.")
    @Size(min = 8, max = 50, message = "비밀번호는 8자 이상 50자 이하로 입력해야 합니다.")
    private String password;

    @NotBlank(message = "국가는 반드시 입력해야 합니다.")
    @Size(max = 20, message = "국가는 최대 20글자까지 가능합니다.")
    private String nationality;

    @NotBlank(message = "모국어는 반드시 입력해야 합니다.")
    @Pattern(
            regexp = "^(ko|en|ja|cn|fr|ar|es|ru)$",
            message = "허용되지 않은 언어 코드입니다. (ko, en, ja, cn, fr, ar, es, ru만 허용)"
    )
    private String native_lang;

    @Size(min = 1, max = 5, message = "학습 언어는 1~5개까지 선택 가능합니다.")
    private List<@Pattern(
            regexp = "^(ko|en|ja|cn|fr|ar|es|ru)$",
            message = "허용되지 않은 언어 코드입니다. (ko, en, ja, cn, fr, ar, es, ru만 허용)"
    ) String> learning;

    @NotBlank(message = "자기소개는 반드시 입력해야 합니다.")
    @Size(max = 50, message = "자기소개는 최대 50글자까지 가능합니다.")
    private String introduction;
}