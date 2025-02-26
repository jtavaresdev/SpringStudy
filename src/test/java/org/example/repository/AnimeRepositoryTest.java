package org.example.repository;

import org.assertj.core.api.Assertions;
import org.example.domain.Anime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Test for Anime Repository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save Persist anime when successful")
    void save_PersistAnime_WhenSuccessful() {
        Anime animeToBeSave = createAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSave);

        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSave.getName());

    }
    @Test
    @DisplayName("Save Update anime when successful")
    void save_UpdatesAnime_WhenSuccessful() {
        Anime animeToBeSave = createAnime();
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
        Anime animeToBeSaved = createAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        this.animeRepository.delete(animeSaved);

        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());

        Assertions.assertThat(animeOptional).isEmpty();

    }
    @Test
    @DisplayName("Find By name anime when successful")
    void findByName_Anime_WhenSuccessuful() {
        Anime animeToBeSaved = createAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        List<Anime> animeList = this.animeRepository.findByName(animeSaved.getName());

        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList).contains(animeSaved);

    }

    public Anime createAnime() {
        return Anime.builder()
                .name("Hajime no Ippo")
                .build();
    }
}