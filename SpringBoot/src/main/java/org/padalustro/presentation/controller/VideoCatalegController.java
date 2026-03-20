package org.padalustro.presentation.controller;

import java.util.List;

import org.padalustro.application.usecase.Cataleg.DeleteVideoCatalegUseCase;
import org.padalustro.application.usecase.Cataleg.GetAllVideoCatalegUseCase;
import org.padalustro.application.usecase.Cataleg.GetVideoCatalegByIdUseCase;
import org.padalustro.application.usecase.Cataleg.SaveVideoCatalegUseCase;
import org.padalustro.application.usecase.Cataleg.UpdateVideoCatalegUseCase;
import org.padalustro.infrastructure.DTO.VideoCatalegDTO;
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
@RequestMapping("/Cataleg")
public class VideoCatalegController {

    private final UpdateVideoCatalegUseCase putVideoCataleg;
    private final SaveVideoCatalegUseCase postVideoCataleg;
    private final GetAllVideoCatalegUseCase getAllVideoCataleg;
    private final GetVideoCatalegByIdUseCase getVideoCatalegById;
    private final DeleteVideoCatalegUseCase deleteVideoCataleg;

    public VideoCatalegController(
            GetAllVideoCatalegUseCase getAllVideoCataleg,
            GetVideoCatalegByIdUseCase getVideoCatalegById,
            DeleteVideoCatalegUseCase deleteVideoCataleg,
            SaveVideoCatalegUseCase postVideoCataleg,
            UpdateVideoCatalegUseCase putVideoCataleg) {

        this.getAllVideoCataleg = getAllVideoCataleg;
        this.getVideoCatalegById = getVideoCatalegById;
        this.deleteVideoCataleg = deleteVideoCataleg;
        this.postVideoCataleg = postVideoCataleg;
        this.putVideoCataleg = putVideoCataleg;
    }



    @GetMapping("/")
    public String helloWorld() {
        return "Benvinguts al Cataleg";
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VideoCatalegDTO>> getAll(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(getAllVideoCataleg.execute());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoCatalegDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getVideoCatalegById.execute(id));
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        deleteVideoCataleg.execute(id);
        return ResponseEntity.noContent().build(); // 204
    }

    @PostMapping
    @CrossOrigin(origins = "*")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> createVideo(@RequestBody VideoCatalegDTO video, @AuthenticationPrincipal Jwt jwt) {
        postVideoCataleg.execute(video);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> updateVideo(
            @PathVariable Long id,
            @RequestBody VideoCatalegDTO video,
            @AuthenticationPrincipal Jwt jwt) {

        boolean updated = putVideoCataleg.execute(id, video);

        if (!updated) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }


}
