package org.padalustro.presentation.controller;

import java.util.List;

import org.padalustro.application.usecase.Estudi.DeleteStudiUseCase;
import org.padalustro.application.usecase.Estudi.GetAllStudiUseCase;
import org.padalustro.application.usecase.Estudi.GetStudiByIdUseCase;
import org.padalustro.application.usecase.Estudi.SaveAllStudiUseCase;
import org.padalustro.application.usecase.Estudi.SaveStudiUseCase;
import org.padalustro.application.usecase.Estudi.UpdateStudiUseCase;
import org.padalustro.infrastructure.DTO.EstudiDTO;
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
@RequestMapping("/Estudi")
public class StudiController {

    private final GetAllStudiUseCase getAllStudi;
    private final SaveStudiUseCase saveStudi;
    private final SaveAllStudiUseCase saveAllStudi;
    private final UpdateStudiUseCase updateStudi;
    private final DeleteStudiUseCase deleteStudi;
    private final GetStudiByIdUseCase getStudiById;


    public StudiController(
            GetAllStudiUseCase getAllStudi,
            SaveStudiUseCase saveStudi,
            SaveAllStudiUseCase saveAllStudi,
            UpdateStudiUseCase updateStudi,
            DeleteStudiUseCase deleteStudi,
            GetStudiByIdUseCase getStudiById) {

        this.getAllStudi = getAllStudi;
        this.saveStudi = saveStudi;
        this.saveAllStudi = saveAllStudi;
        this.updateStudi = updateStudi;
        this.deleteStudi = deleteStudi;
        this.getStudiById = getStudiById;
    }

    
    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<EstudiDTO>> getAll() {
        return ResponseEntity.ok(getAllStudi.execute());
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> create(@RequestBody EstudiDTO studi, @AuthenticationPrincipal Jwt jwt) {
        saveStudi.execute(studi);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/varios")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> createAll(@RequestBody List<EstudiDTO> estudis, @AuthenticationPrincipal Jwt jwt) {
        saveAllStudi.execute(estudis);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/{id}")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody EstudiDTO studi,
            @AuthenticationPrincipal Jwt jwt) {

        boolean updated = updateStudi.execute(id, studi);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        deleteStudi.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudiDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getStudiById.execute(id));
    }
}