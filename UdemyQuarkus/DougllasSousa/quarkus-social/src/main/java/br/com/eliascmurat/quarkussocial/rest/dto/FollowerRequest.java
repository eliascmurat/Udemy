package br.com.eliascmurat.quarkussocial.rest.dto;

import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class FollowerRequest {
    @Positive(message = "FollowerId must be a positive number")
    private Long followerId;
}
