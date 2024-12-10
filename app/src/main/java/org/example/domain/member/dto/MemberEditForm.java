package org.example.domain.member.dto;

import lombok.Data;

import java.util.List;

@Data
public class MemberEditForm {
    private String username;
    private String nationality;
    private String native_lang;
    private List<String> learning;
    private String introduction;
}
