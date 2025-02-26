package org.example.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AnimePostRequestBody {
    private @NotBlank String name;

}
