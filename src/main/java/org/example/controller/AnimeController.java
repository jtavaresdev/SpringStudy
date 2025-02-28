package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.domain.Anime;
import org.example.requests.AnimePostRequestBody;
import org.example.requests.AnimePutRequestBody;
import org.example.service.AnimeService;
import org.example.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


// All args contructos (lombok)
@RestController
@RequestMapping("animes")
@RequiredArgsConstructor
@Log4j2
public class AnimeController {

    @Autowired
    private final DateUtil dateUtil;
    @Autowired
    private final AnimeService animeService;


    @GetMapping("/")
    public ResponseEntity<Page<Anime>> list(Pageable pageable) {
        log.info(dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Anime>> listAll() {
        log.info(dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAllNoPageable(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id) {
        log.info(dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.findByIdOrElseThrow(id), HttpStatus.OK);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam(required = true) String name) {
        log.info(dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return ResponseEntity.status(HttpStatus.OK).body(animeService.findByName(name));

    }

    @PostMapping("/")
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody) {
        log.info(dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return ResponseEntity.status(HttpStatus.CREATED).body(animeService.save(animePostRequestBody));
    }

    @PutMapping("/")
    public ResponseEntity<Object> update(@RequestBody AnimePutRequestBody animePutRequestBody) {
        animeService.replace(animePutRequestBody);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Updated");
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Anime> deleteById(@PathVariable Long id) {
        log.info(dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
