package org.padalustro.presentation.controller;

import java.util.List;

import org.padalustro.application.usecase.Category.DeleteCategoryUseCase;
import org.padalustro.application.usecase.Category.GetAllCategoryUseCase;
import org.padalustro.application.usecase.Category.GetCategoryByIdUseCase;
import org.padalustro.application.usecase.Category.SaveAllCategoryUseCase;
import org.padalustro.application.usecase.Category.SaveCategoryUseCase;
import org.padalustro.application.usecase.Category.UpdateCategoryUseCase;
import org.padalustro.infrastructure.DTO.CategoriaDTO;
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
@RequestMapping("/Category")
public class CategoryController {

    private final GetAllCategoryUseCase getAllCategory;
    private final SaveCategoryUseCase saveCategory;
    private final SaveAllCategoryUseCase saveAllCategory;
    private final UpdateCategoryUseCase updateCategory;
    private final DeleteCategoryUseCase deleteCategory;
    private final GetCategoryByIdUseCase getCategoryById;
    
    public CategoryController(
            GetAllCategoryUseCase getAllCategory,
            SaveCategoryUseCase saveCategory,
            SaveAllCategoryUseCase saveAllCategory,
            UpdateCategoryUseCase updateCategory,
            DeleteCategoryUseCase deleteCategory,
            GetCategoryByIdUseCase getCategoryById) {

        this.getAllCategory = getAllCategory;
        this.saveCategory = saveCategory;
        this.saveAllCategory = saveAllCategory;
        this.updateCategory = updateCategory;
        this.deleteCategory = deleteCategory;
        this.getCategoryById = getCategoryById;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> getAll() {
        return ResponseEntity.ok(getAllCategory.execute());
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> create(@RequestBody CategoriaDTO categoria, @AuthenticationPrincipal Jwt jwt) {
        saveCategory.execute(categoria);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/varios")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> createAll(@RequestBody List<CategoriaDTO> categories, @AuthenticationPrincipal Jwt jwt) {
        saveAllCategory.execute(categories);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/{id}")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody CategoriaDTO categoria, @AuthenticationPrincipal Jwt jwt) {
        boolean updated = updateCategory.execute(id, categoria);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    @PreAuthorize("#jwt.claims['is_admin'] == true")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        deleteCategory.execute(id);
        return ResponseEntity.noContent().build();
    }


     @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getCategoryById.execute(id));
    }
}