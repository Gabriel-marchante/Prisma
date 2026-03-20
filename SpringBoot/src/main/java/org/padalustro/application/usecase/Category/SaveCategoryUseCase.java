package org.padalustro.application.usecase.Category;

import org.padalustro.domain.repository.CategoriaRepository;
import org.padalustro.infrastructure.DTO.CategoriaDTO;
import org.springframework.stereotype.Component;

@Component
public class SaveCategoryUseCase {

    private final CategoriaRepository repository;

    public SaveCategoryUseCase(CategoriaRepository repository) {
        this.repository = repository;
    }

    public void execute(CategoriaDTO categoria) {
        repository.save(categoria);
    }
}
