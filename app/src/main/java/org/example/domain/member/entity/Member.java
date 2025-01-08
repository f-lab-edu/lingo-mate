package org.example.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.language.Language;
import org.example.domain.member.dto.request.MemberEditForm;
import org.example.domain.member.dto.request.MemberJoinForm;
import org.example.domain.question.entity.Question;
import org.example.domain.wordbook.entity.WordBook;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String username;
    private String password;
    private String nationality;
    private String nativeLang;
    private String introduction;
    private int follower;
    private int following;
    private int point;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Language> learnings = new ArrayList<Language>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Question> questions = new ArrayList<Question>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WordBook> wordbooks = new ArrayList<WordBook>();

    //  생성 메서드
    static public Member createMember(MemberJoinForm memberForm) {
        Member member = Member.builder()
                .email(memberForm.getEmail())
                .username(memberForm.getUsername())
                .password(memberForm.getPassword())
                .nationality(memberForm.getNationality())
                .nativeLang(memberForm.getNative_lang())
                .introduction(memberForm.getIntroduction())
                .follower(0)
                .following(0)
                .point(0)
                .build();
        return member;
    }

    // 임시 수정 메서드
    public Member editMember(MemberEditForm form) {
        if (form.getUsername() != null) this.username = form.getUsername();
        if (form.getNationality() != null) this.nationality = form.getNationality();
        if (form.getNativeLang() != null) this.nativeLang = form.getNativeLang();
        if (form.getIntroduction() != null) this.introduction = form.getIntroduction();
        return this;
    }

    // 연관관계 메서드
    public void addLearning(Language language) {
        this.learnings.add(language);
        language.setMember(this); // 연관관계 주인 쪽에도 설정
    }

    // 연관관계 초기화 메서드
    public void clearLearnings() {
        for (Language language : this.learnings) {
            language.setMember(null); // 연관 관계 제거
        }
        this.learnings.clear(); // 리스트 비우기
    }
    public void removeLearning(Language language) {
        this.learnings.remove(language);
        language.setMember(null); // 연관관계 제거
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
        question.setMember(this); // 연관관계 주인 쪽에도 설정
    }

    public void removeQuestion(Question question) {
        this.questions.remove(question);
        question.setMember(null); // 연관관계 제거
    }

    public void addWordBook(WordBook wordBook) {
        this.wordbooks.add(wordBook);
        wordBook.setMember(this); // 연관관계 주인 쪽에도 설정
    }

    public void removeWordBook(WordBook wordBook) {
        this.wordbooks.remove(wordBook);
        wordBook.setMember(null); // 연관관계 제거
    }
}
