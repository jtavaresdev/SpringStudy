package org.example.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.domain.Anime;
import org.example.exception.BadRequestException;
import org.example.mapper.AnimeMapper;
import org.example.repository.AnimeRepository;
import org.example.requests.AnimePostRequestBody;
import org.example.requests.AnimePutRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class AnimeService {

    @Autowired
    private AnimeRepository animeRepository;

    public Page<Anime> listAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }
    public List<Anime> findByName(String name) {
        List<Anime> byName = animeRepository.findByName(name);
        if (byName.isEmpty()){
            throw new BadRequestException("Erro trying find anime by name");
        }
        return byName;
    }

    public Anime findByIdOrElseThrow(Long id) {
        return animeRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException("Error trying to find"));

    }

    @Transactional
    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(AnimeMapper.INSTACE.toAnime(animePostRequestBody));
    }

    public void delete(Long id) {
        Anime byIdOrElseThrow = findByIdOrElseThrow(id);
        animeRepository.delete(byIdOrElseThrow);
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime byIdOrElseThrow = findByIdOrElseThrow(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTACE.toAnime(animePutRequestBody);
        anime.setId(byIdOrElseThrow.getId());
        animeRepository.save(anime);
    }

    public List<Anime> listAllNoPageable() {
        return animeRepository.findAll();
    }
}

