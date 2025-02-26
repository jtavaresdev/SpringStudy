package org.example.client;

import lombok.extern.log4j.Log4j2;
import org.example.domain.Anime;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(entity);

        Anime forObject = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(forObject);

        Anime[] arrayAnime = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info(Arrays.toString(arrayAnime));

        //SuperType Tokken


        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                });
        log.info(exchange.getBody());
//
//        Anime anime = Anime.builder().name("Lord of the mysteries").build();
//
//        Anime animeSaved = new RestTemplate().postForObject("http://localhost:8080/animes/", anime, Anime.class);
//        log.info (animeSaved);


        Anime anime = Anime.builder().name("Samurai Champlo").build();

        ResponseEntity<Anime> exchange1 = new RestTemplate().exchange(
                "http://localhost:8080/animes/",
                HttpMethod.POST,
                new HttpEntity<>(anime),
                new ParameterizedTypeReference<Anime>() {
                });
        log.info("Saved anime {}", exchange1);

        Anime wbody = exchange1.getBody();
        wbody.setName("Samurai Champlo 2");

        ResponseEntity<Anime> exchangeUpdate = new RestTemplate().exchange(
                "http://localhost:8080/animes/",
                HttpMethod.PUT,
                new HttpEntity<>(wbody),
                Anime.class);
        ResponseEntity<Void> exchangeDelete = new RestTemplate().exchange
                ("http://localhost:8080/animes/{id}",
                        HttpMethod.DELETE, null, Void.class, wbody.getId());

        log.info("Anime deleted{}", exchangeDelete);

    }
}
