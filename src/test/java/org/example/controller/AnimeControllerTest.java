package org.example.controller;

import org.assertj.core.api.Assertions;
import org.example.domain.Anime;
import org.example.exception.BadRequestException;
import org.example.requests.AnimePostRequestBody;
import org.example.requests.AnimePutRequestBody;
import org.example.service.AnimeService;
import org.example.util.AnimeCreator;
import org.example.util.AnimePostRequestBodyCreator;
import org.example.util.AnimePutRequestBodyCreator;
import org.example.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;
    @Mock
    private DateUtil dateUtil;


    @BeforeEach
    void setUp() {
        PageImpl<Anime> anime = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(anime);

        BDDMockito.when(animeServiceMock.listAllNoPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.findByIdOrElseThrow(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());


        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());


        // Metodo void
        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));
        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("Replace updates anime when successful")
    void Replace_UpdatesAnime_WhenSuccessful() {
        Assertions.assertThatCode(() ->  animeController.update(AnimePutRequestBodyCreator.createAnimePostRequestBody()))
                        .doesNotThrowAnyException();
        ResponseEntity<Object> entity = animeController.update(AnimePutRequestBodyCreator.createAnimePostRequestBody());
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("Delete anime when successful")
    void Delete_RemoveAnime_WhenSuccessful() {
        Assertions.assertThatCode(() ->  animeController.deleteById(1L))
                .doesNotThrowAnyException();
        ResponseEntity<Anime> entity = animeController.deleteById(1l);
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnLIstOfAnimesInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeController.list(null).getBody();
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void listAll_ReturnLIstOfAnimes_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animeList = animeController.listAll().getBody();
        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find by id returns  anime when successful")
    void findByIdOrElseThrow_ReturnAnime_WhenSuccessful() {
        Long animeExpected = AnimeCreator.createValidAnime().getId();
        Anime anime = animeController.findById(1L).getBody();
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(animeExpected);
    }

    @Test
    @DisplayName("Find by name returns list of anime when successful")
    void findByName_ReturnListOfAnime_WhenSuccessful() {
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));
        String animeExpected = AnimeCreator.createValidAnime().getName();
        List<Anime> byName = animeServiceMock.findByName("Hajime no Ippo");
        Assertions.assertThat(byName).isNotEmpty();
        Assertions.assertThat(byName.get(0).getName()).isNotNull().isEqualTo(animeExpected);
    }

    @Test
    @DisplayName("Save returns  anime when successful")
    void Save_ReturnAnime_WhenSuccessful() {
        Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimePostRequestBody()).getBody();
        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
    }
}