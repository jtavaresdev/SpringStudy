package org.example.repository;

import org.assertj.core.api.Assertions;
import org.example.domain.Anime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.example.util.AnimeCreator.createAnimeToBeSaved;

@DataJpaTest
@DisplayName("Test for Anime Repository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save Persist anime when successful")
    void save_PersistAnime_WhenSuccessful() {
        Anime animeToBeSave = createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToBeSave);

        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSave.getName());

    }
    @Test
    @DisplayName("Save Update anime when successful")
    void save_UpdatesAnime_WhenSuccessful() {
        Anime animeToBeSave = createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToBeSave);

        animeSaved.setName("Wind Breaker");

        Anime animeUpdate = this.animeRepository.save(animeSaved);

        Assertions.assertThat(animeUpdate).isNotNull();
        Assertions.assertThat(animeUpdate.getId()).isNotNull();
        Assertions.assertThat(animeUpdate.getName()).isEqualTo(animeSaved.getName());

    }
    @Test
    @DisplayName("Delete anime when successful")
    void delete_Anime_WhenSuccessuful() {
        Anime animeToBeSaved = createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        this.animeRepository.delete(animeSaved);

        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());

        Assertions.assertThat(animeOptional).isEmpty();

    }
    @Test
    @DisplayName("Find By name anime when successful")
    void findByName_ReturnList_WhenSuccessuful() {
        Anime animeToBeSaved = createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        List<Anime> animeList = this.animeRepository.findByName(animeSaved.getName());

        Assertions.assertThat(animeList)
                .isNotEmpty()
                .contains(animeSaved);
    }
    @Test
    @DisplayName("Find By name anime when no anime is found")
    void findByName_ReturnsEmptyList_WhenNotSuccessuful() {

        List<Anime> animeList = this.animeRepository.findByName("Anime test");

        Assertions.assertThat(animeList).isEmpty();

    }
    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty(){
        Anime anime = new Anime();
        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
                .isInstanceOf(ConstraintViolationException.class);

    }

}