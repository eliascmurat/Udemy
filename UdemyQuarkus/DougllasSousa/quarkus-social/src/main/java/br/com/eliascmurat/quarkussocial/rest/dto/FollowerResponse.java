package br.com.eliascmurat.quarkussocial.rest.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.eliascmurat.quarkussocial.domain.model.Follower;
import lombok.Data;

@Data
public class FollowerResponse {
    private Long id;
    private String name;

    public static FollowerResponse fromEntity(Follower follower) {
        FollowerResponse followerResponse = new FollowerResponse();
        followerResponse.setId(follower.getId());
        followerResponse.setName(follower.getFollowerUser().getName());

        return followerResponse;
    }
    public static List<FollowerResponse> fromEntity(List<Follower> followers) {
        return followers.stream().map(FollowerResponse::fromEntity).collect(Collectors.toList());
    }
}
