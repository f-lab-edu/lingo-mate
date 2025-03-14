package org.example.domain.wordbook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.wordbook.entity.Wordbook;

@Data
@NoArgsConstructor
public class WordbookResponse {
    private String username;
    private String title;
    private String description;

    private WordbookResponse(Wordbook wordbook) {
        this.username = wordbook.getMember().getUsername();
        this.title = wordbook.getTitle();
        this.description = wordbook.getDescription();
    }

    public static WordbookResponse create(Wordbook wordbook){
        return new WordbookResponse(wordbook);
    }
}
