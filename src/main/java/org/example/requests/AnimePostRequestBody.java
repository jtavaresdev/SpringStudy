package org.example.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class AnimePostRequestBody {
    public AnimePostRequestBody() {
    }

    public AnimePostRequestBody(String name) {
        this.name = name;
    }

    private @NotBlank String name;


}
