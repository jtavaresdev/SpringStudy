package org.example.service;

import org.assertj.core.api.Assertions;
import org.example.domain.Anime;
import org.example.exception.BadRequestException;
import org.example.repository.AnimeRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;
    @Mock
    private DateUtil dateUtil;


    @BeforeEach
    void setUp() {
        PageImpl<Anime> anime = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(anime);

        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());


        // Metodo void
        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("listAll returns list of anime inside page object when successful")
    void listAll_ReturnLIstOfAnimesInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,2));
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void listAll_ReturnLIstOfAnimes_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animeList = animeService.listAllNoPageable();
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
        Anime anime = animeService.findByIdOrElseThrow(1L);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(animeExpected);
    }
    @Test
    @DisplayName("Find by id returns Bad Request Exception when anime is not found successful")
    void findByIdOrElseThrow_BadRequestException_WhenNotFindAnime() {
        BDDMockito.when(animeService.findByIdOrElseThrow(0L)).thenThrow(BadRequestException.class);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByIdOrElseThrow(0L));

    }

    @Test
    @DisplayName("Find by name returns list of anime when successful")
    void findByName_ReturnListOfAnime_WhenSuccessful() {
        String animeExpected = AnimeCreator.createValidAnime().getName();
        List<Anime> byName = animeService.findByName("Hajime no Ippo");
        Assertions.assertThat(byName).isNotEmpty();
        Assertions.assertThat(byName.get(0).getName()).isNotNull().isEqualTo(animeExpected);
    }
    @Test
    @DisplayName("Find by name returns list empty of anime when not found anime")
    void findByName_ReturnListEmptyOfAnime_WhenNotSuccessful() {
        BDDMockito.when(animeService.findByName(" ")).thenThrow(BadRequestException.class);

        Assertions.assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> animeService.findByName(" "));
    }

    @Test
    @DisplayName("Save returns  anime when successful")
    void Save_ReturnAnime_WhenSuccessful() {
        Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimePostRequestBody());
        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("Replace updates anime when successful")
    void Replace_UpdatesAnime_WhenSuccessful() {
        Assertions.assertThatCode(() ->  animeService.replace(AnimePutRequestBodyCreator.createAnimePostRequestBody()))
                .doesNotThrowAnyException();

    }
    @Test
    @DisplayName("Delete anime when successful")
    void Delete_RemoveAnime_WhenSuccessful() {
        Assertions.assertThatCode(() ->  animeService.delete(1L))
                .doesNotThrowAnyException();

    }

}