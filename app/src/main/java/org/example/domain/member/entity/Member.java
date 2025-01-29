package org.example.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.auth.entity.AuthEntity;
import org.example.domain.comment.Comment;
import org.example.domain.language.Language;
import org.example.domain.member.dto.request.MemberEditRequest;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.question.entity.Question;
import org.example.domain.wordbook.entity.WordBook;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    private Role role;
    private int follower;
    private int following;
    private int point;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Language> learnings = new ArrayList<Language>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<Question>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WordBook> wordbooks = new ArrayList<WordBook>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private AuthEntity authEntity;
    private Member(final MemberJoinRequest memberJoinRequest) {
        this.email = memberJoinRequest.getEmail();
        this.username = memberJoinRequest.getUsername();
        this.password = memberJoinRequest.getPassword();
        this.nationality = memberJoinRequest.getNationality();
        this.nativeLang = memberJoinRequest.getNativeLang();
        this.introduction = memberJoinRequest.getIntroduction();
        this.role = Role.USER;
        this.follower = 0;
        this.following = 0;
        this.point = 50;
    }


    //  생성 메서드
    static public Member createMember(final MemberJoinRequest memberJoinRequest) {
        return new Member(memberJoinRequest);
    }


    // 임시 수정 메서드
    public Member editMember(final MemberEditRequest request) {
        if (request.getUsername() != null) this.username = request.getUsername();
        if (request.getNationality() != null) this.nationality = request.getNationality();
        if (request.getNativeLang() != null) this.nativeLang = request.getNativeLang();
        if (request.getIntroduction() != null) this.introduction = request.getIntroduction();
        return this;
    }

    // 연관관계 메서드
    public void addLearning(Language language) {
        this.learnings.add(language);
        language.setMember(this); // 연관관계 주인 쪽에도 설정
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setMember(this);
    }

    // 연관관계 초기화 메서드
    public void clearLearnings() {
        for (Language language : this.learnings) {
            language.setMember(null); // 연관 관계 제거
        }
        this.learnings.clear(); // 리스트 비우기
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
