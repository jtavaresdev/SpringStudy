package org.example.integration;


import org.assertj.core.api.Assertions;
import org.example.domain.Anime;
import org.example.exception.BadRequestException;
import org.example.repository.AnimeRepository;
import org.example.requests.AnimePostRequestBody;
import org.example.util.AnimeCreator;
import org.example.util.AnimePostRequestBodyCreator;
import org.example.util.AnimePutRequestBodyCreator;
import org.example.wrapper.PageableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//DataJpa remove os dados para cada test
// AutoCOnfigureTestDatabaase nao remove

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("list returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes/", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void listAll_ReturnLIstOfAnimesInsidePageObject_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        List<Anime> animeList = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);


    }


//
    @Test
    @DisplayName("Find by id returns  anime when successful")
    void findByIdOrElseThrow_ReturnAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        Long animeExpected = savedAnime.getId();
        Anime anime = testRestTemplate.getForObject("/animes/{id}",Anime.class,animeExpected);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(animeExpected);
    }

    @Test
    @DisplayName("Find by name returns list of anime when successful")
    void findByName_ReturnListOfAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String animeExpected = savedAnime.getName();
        String url = String.format("/animes/find?name=%s",animeExpected);

        List<Anime> byName = testRestTemplate.exchange(url,HttpMethod.GET,null,
                new ParameterizedTypeReference<List<Anime>>(){}).getBody();

        Assertions.assertThat(byName).isNotNull();
        Assertions.assertThat(byName.get(0).getName()).isNotEmpty().isEqualTo(animeExpected);
    }
    @Test
    @DisplayName("Find by name returns list empty of anime when not found anime")
    void findByName_ReturnListEmptyOfAnime_WhenNotSuccessful() {

        List<Anime> byName = testRestTemplate.exchange("/animes/find?name=dbz",HttpMethod.GET,null,
                new ParameterizedTypeReference<List<Anime>>(){}).getBody();

        Assertions.assertThat(byName).isEmpty();
    }

    @Test
    @DisplayName("Save returns  anime when successful")
    void Delete_ReturnNoContendHttpStatus_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        Long id = savedAnime.getId();
        Anime anime = testRestTemplate.exchange("/animes/{id}", HttpMethod.DELETE, null,
                new ParameterizedTypeReference<Anime>() {
                }, id).getBody();

        Assertions.assertThat(anime).isNull();
    }

    @Test
    @DisplayName("Save returns  anime when successful")
    void Save_ReturnAnime_WhenSuccessful() {
        AnimePostRequestBody savedAnime = AnimePostRequestBodyCreator.createAnimePostRequestBody();

        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes/", savedAnime, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("Replace updates anime when successful")
    void Replace_UpdatesAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createValidAnime());

        savedAnime.setName("New NAME");

        ResponseEntity<Void> entity = testRestTemplate.exchange("/animes/",
                HttpMethod.PUT,
                new HttpEntity<>(savedAnime),
                Void.class);


        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(entity).isNotNull();

    }

}