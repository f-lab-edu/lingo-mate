package org.example.domain.question.fixture;

import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.response.QuestionResponse;
import org.example.domain.question.dto.request.QuestionCreateRequest;
import org.example.domain.question.dto.request.QuestionEditRequest;
import org.example.domain.question.entity.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionTestFixture {

    public static Member createMockMember() {
        MemberJoinRequest request = MemberJoinRequest.builder().email("qwer@qwer.com").build();
        Member member = Member.createMember(request);
        member.setId(0L);
        return member;
    }
    public static QuestionCreateRequest createQuestionCreateRequest(){
        return QuestionCreateRequest.builder()
                .questionLanguage("en") // 허용된 언어 코드
                .title("what does slay means?") // 유효한 제목
                .content("slay? what mean?") // 유효한 내용
                .point(50) // 포인트 (0 ~ 100 범위 내)
                .build();
    }

    public static QuestionEditRequest createQuestionEditRequest(){
        return QuestionEditRequest.builder().questionLanguage("ja").title("ありがとう").content("what means?").build();
        // 기존 포인트 유지
    }
    public static Question createQuestion() {
        Question question = Question.create(QuestionTestFixture.createQuestionCreateRequest());
        Member mockMember = createMockMember();
        question.setMember(mockMember);
        question.setId(0L);
        return question;
    }
    public static QuestionResponse createQuestionResponse(){
        return QuestionResponse.create(QuestionTestFixture.createQuestion());
    }

    public static List<QuestionResponse> createQuestionResponse20() {
        ArrayList<QuestionResponse> responses = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            responses.add(createQuestionResponse());
        }

        return responses;
    }

    public static List<Question> createQuestion20() {
        ArrayList<Question> questions = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            questions.add(createQuestion());
        }

        return questions;
    }
}
