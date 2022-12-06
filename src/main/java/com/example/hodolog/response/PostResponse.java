package com.example.hodolog.response;

import com.example.hodolog.domain.Post;
import lombok.*;

@Getter
@NoArgsConstructor
@Setter
public class PostResponse {

    private Long id;
    private String title;
    private String content;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
