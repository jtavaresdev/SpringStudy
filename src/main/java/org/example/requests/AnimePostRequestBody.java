package org.example.requests;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "This is the Anime name", example = "Re:Zero")
    private @NotBlank String name;


}
