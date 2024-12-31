package org.example.domain.question;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.question.dto.request.QuestionCreateForm;
import org.example.domain.question.entity.Question;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.domain.question.entity.Question.createQuestion;

@Slf4j
@Repository
public class QuestionRepository {

    private static ConcurrentHashMap<Long, Question> store = new ConcurrentHashMap<>();

    public Question save(QuestionCreateForm createForm, String loggedUsername) {
        Question newQuestion = createQuestion(createForm, loggedUsername);
        store.put(newQuestion.getId(), newQuestion);
        return newQuestion;
    }

    public Question findById(Long q_id) {
        return store.get(q_id);
    }

    public List<Question> findAll() {
        return new ArrayList<>(store.values());
    }

    public Question deleteById(Long q_id) {
        return store.remove(q_id);
    }

    public List<Question> findByKeyword(String keyword) {
        ArrayList<Question> questions = new ArrayList<>(store.values());
        ArrayList<Question> searchedQuestion = new ArrayList<>();
        for (Question question : questions) {
            String content = question.getContent();
            String title = question.getTitle();

            if(content.contains(keyword) || title.contains(keyword)){
                searchedQuestion.add(question);
            }
        }
        return searchedQuestion;
    }

    public void cleanStore() {
        store.clear();
    }


}
