package org.example.domain.member.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberEditRequest {

    @Size(max = 20, message = "사용자 이름은 최대 20글자 입니다.")
    private String username;

    @Size(max = 20, message = "국가는 최대 20글자까지 가능합니다.")
    private String nationality;

    @Pattern(
            regexp = "^(ko|en|ja|cn|fr|ar|es|ru)$",
            message = "허용되지 않은 언어 코드입니다. (ko, en, ja, cn, fr, ar, es, ru만 허용)"
    ) private String nativeLang;

    @Size(min = 1, max = 5, message = "학습 언어는 1~5개까지 선택 가능합니다.") // 리스트 전체에 대한 검증
    private List<@Pattern(
            regexp = "^(ko|en|ja|cn|fr|ar|es|ru)$",
            message = "허용되지 않은 언어 코드입니다. (ko, en, ja, cn, fr, ar, es, ru만 허용)"
    ) String> learning;

    @Size(max = 50, message = "자기소개는 최대 50글자 입니다.")
    private String introduction;
}
