package org.padalustro.presentation.controller;

import java.util.List;

import org.padalustro.application.usecase.Serie.DeleteSerieUseCase;
import org.padalustro.application.usecase.Serie.GetAllSerieUseCase;
import org.padalustro.application.usecase.Serie.GetSerieByIdUseCase;
import org.padalustro.application.usecase.Serie.SaveAllSerieUseCase;
import org.padalustro.application.usecase.Serie.SaveSerieUseCase;
import org.padalustro.application.usecase.Serie.UpdateSerieUseCase;
import org.padalustro.infrastructure.DTO.SerieDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Serie")
public class SerieController {

    private final GetAllSerieUseCase getAllSerie;
    private final SaveSerieUseCase saveSerie;
    private final SaveAllSerieUseCase saveAllSerie;
    private final UpdateSerieUseCase updateSerie;
    private final DeleteSerieUseCase deleteSerie;
    private final GetSerieByIdUseCase getSerieById;

    public SerieController(
            GetAllSerieUseCase getAllSerie,
            SaveSerieUseCase saveSerie,
            SaveAllSerieUseCase saveAllSerie,
            UpdateSerieUseCase updateSerie,
            DeleteSerieUseCase deleteSerie,
            GetSerieByIdUseCase getSerieById) {

        this.getAllSerie = getAllSerie;
        this.saveSerie = saveSerie;
        this.saveAllSerie = saveAllSerie;
        this.updateSerie = updateSerie;
        this.deleteSerie = deleteSerie;
        this.getSerieById = getSerieById;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<SerieDTO>> getAll() {
        return ResponseEntity.ok(getAllSerie.execute());
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> create(@RequestBody SerieDTO serie, @AuthenticationPrincipal Jwt jwt) {
        saveSerie.execute(serie);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/varios")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> createAll(@RequestBody List<SerieDTO> series, @AuthenticationPrincipal Jwt jwt) {
        saveAllSerie.execute(series);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/{id}")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody SerieDTO serie,
            @AuthenticationPrincipal Jwt jwt) {

        boolean updated = updateSerie.execute(id, serie);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        deleteSerie.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SerieDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getSerieById.execute(id));
    }
}
