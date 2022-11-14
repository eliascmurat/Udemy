package br.com.eliascmurat.quarkussocial.rest.dto;

import lombok.Getter;

@Getter
public class FieldError {
    private String field;
    private String error;

    public FieldError(String field, String error) {
        this.field = field;
        this.error = error;
    }
}
