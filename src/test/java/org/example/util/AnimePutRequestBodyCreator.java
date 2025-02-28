package org.example.util;

import org.example.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {

    public static AnimePutRequestBody createAnimePostRequestBody() {
        return AnimePutRequestBody.builder()
                .name(AnimeCreator.createValidUpdatedAnime().getName())
                .id(AnimeCreator.createValidUpdatedAnime().getId())
                .build();
    }
}
