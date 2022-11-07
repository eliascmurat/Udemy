package br.com.eliascmurat.quarkussocial.rest.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreatePostRequest {
    @NotBlank(message = "Text is required")
    private String text;
}
