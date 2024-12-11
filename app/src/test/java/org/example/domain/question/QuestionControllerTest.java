package org.example.domain.question;

import org.example.domain.question.dto.Comment;
import org.example.domain.question.dto.Question;
import org.example.domain.question.dto.QuestionCreateForm;
import org.example.domain.question.dto.QuestionEditForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private QuestionRepository questionRepository;

    // 테스트 질문 생성
    public Question createQuestion() {

        QuestionCreateForm createForm = new QuestionCreateForm();
        createForm.setAuthor("JohnDoe");
        createForm.setQuestion_language("English");
        createForm.setTitle("What is Spring Boot?");
        createForm.setContent("I want to know about Spring Boot.");
        createForm.setPoint(10);

        return new Question(createForm);
    }

    // 테스트 댓글 생성
    public Comment createComment() {
        return new Comment("comment", "james");
    }

    @Test
    @DisplayName("질문 생성 테스트")
    void createQuestionTest() throws Exception {

        // 질문 생성
        Question question = createQuestion();
        question.setId(1L);

        when(questionRepository.save(any(QuestionCreateForm.class))).thenReturn(question);

        mockMvc.perform(post("/question/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("author", "JohnDoe")
                        .param("question_language", "English")
                        .param("title", "What is Spring Boot?")
                        .param("content", "I want to know about Spring Boot.")
                        .param("point", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    @DisplayName("질문 조회 테스트")
    void findQuestionTest() throws Exception {

        //질문 생성
        Question question = createQuestion();
        question.setId(1L);
        when(questionRepository.findById(1L)).thenReturn(question);

        mockMvc.perform(get("/question/{q_id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.author").value("JohnDoe"))
                .andExpect(jsonPath("$.title").value("What is Spring Boot?"));
    }

    @Test
    @DisplayName("질문 수정 테스트")
    void editQuestionTest() throws Exception {

        // 질문 생성
        Question question = createQuestion();
        question.setId(1L);

        // 질문 수정
        QuestionEditForm questionEditForm = new QuestionEditForm();
        questionEditForm.setQuestion_language("French");
        questionEditForm.setTitle("updated title");
        Question editedQuestion = question.editQuestion(questionEditForm);


        when(questionRepository.findById(1L)).thenReturn(question);
        mockMvc.perform(put("/question/{q_id}/edit", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("question_language", "french")
                .param("title", "updated title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question_language").value("french"))
                .andExpect(jsonPath("$.title").value("updated title"));

    }

    @Test
    @DisplayName("잘문 삭제 테스트")
    void deleteQuestionTest() throws Exception {
        Question question = createQuestion();
        question.setId(1L);
        when(questionRepository.deleteById(1L)).thenReturn(question);

        mockMvc.perform(delete("/question/{q_id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("질문 검색 테스트")
    void searchQuestionTest() throws Exception {
        Question question1 = createQuestion();
        question1.setId(1L);
        question1.setContent("this is a test");

        Question question2 = createQuestion();
        question2.setId(2L);
        question2.setContent("title is test");

        Question question3 = createQuestion();
        question3.setId(3L);

        List<Question> list = Arrays.asList(question1, question2, question3);
        when(questionRepository.findByKeyword("test")).thenReturn(list);

        mockMvc.perform(get("/question/search?keyword=test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("질문 댓글 추가 테스트")
    void addCommentTest() throws Exception {

        Question question = createQuestion();
        question.setId(1L);
        when(questionRepository.findById(1L)).thenReturn(question);

        mockMvc.perform(post("/question/{q_id}/comments",1L)
                        .param("comment", "comment")
                        .param("author", "james"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].comment").value("comment"))
                .andExpect(jsonPath("$.comments[0].author").value("james"));
    }

    @Test
    @DisplayName("질문 댓글 수정 테스트")
    void updateCommentTest() throws Exception {

        Question question = createQuestion();
        question.setId(1L);
        Comment comment = createComment();
        comment.setId(1L);
        question.addComment(comment);

        when(questionRepository.findById(1L)).thenReturn(question);

        mockMvc.perform(put("/question/{q_id}/comments/{c_id}",1L,1L)
                .param("comment", "newComment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].comment").value("newComment"));
    }

    @Test
    @DisplayName("질문 댓글 삭제 테스트")
    void deleteCommentTest() throws Exception {
        Question question = createQuestion();
        question.setId(1L);
        Comment comment = createComment();
        comment.setId(1L);
        question.addComment(comment);

        when(questionRepository.findById(1L)).thenReturn(question);

        mockMvc.perform(delete("/question/{q_id}/comments/{c_id}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("질문 목록 조회 테스트")
    void findAllQuestionsTest() throws Exception {
        Question question1 = createQuestion();
        question1.setId(1L);
        Question question2 = createQuestion();
        question2.setId(2L);
        List<Question> questions = Arrays.asList(question1, question2);

        when(questionRepository.findAll()).thenReturn(questions);

        mockMvc.perform(get("/question/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }
}