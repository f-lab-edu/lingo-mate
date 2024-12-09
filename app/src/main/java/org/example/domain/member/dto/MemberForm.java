package org.example.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class MemberForm {
    private String email;
    private String username;
    private String password;
    private String nationality;
    private String native_lang;
    private List<String> learning;
    private String introduction;
}
