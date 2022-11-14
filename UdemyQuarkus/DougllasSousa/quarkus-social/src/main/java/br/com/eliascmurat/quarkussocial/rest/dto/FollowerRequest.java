package br.com.eliascmurat.quarkussocial.rest.dto;

import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowerRequest {
    @Positive(message = "FollowerId must be a positive number")
    private Long followerId;
}
