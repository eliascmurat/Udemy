package br.com.eliascmurat.quarkussocial.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.com.eliascmurat.quarkussocial.domain.model.Post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {
    private String text;
    private LocalDateTime dateTime;

    public static PostResponse fromEntity(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setText(post.getText());
        postResponse.setDateTime(post.getDateTime());

        return postResponse;
    }

    public static List<PostResponse> fromEntity(List<Post> posts) {
        return posts.stream().map(PostResponse::fromEntity).collect(Collectors.toList());
    }
}
