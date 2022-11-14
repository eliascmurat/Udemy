package br.com.eliascmurat.quarkussocial.rest.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FollowersPerUserResponse {
    private Integer followersCount;
    private List<FollowerResponse> content;
}
