package org.padalustro.application.usecase.Category;

import org.padalustro.domain.repository.CategoriaRepository;
import org.padalustro.infrastructure.DTO.CategoriaDTO;
import org.springframework.stereotype.Component;

@Component
public class UpdateCategoryUseCase {

    private final CategoriaRepository repository;

    public UpdateCategoryUseCase(CategoriaRepository repository) {
        this.repository = repository;
    }

    public boolean execute(Long id, CategoriaDTO categoria) {
        if (repository.findAllDTO().stream().noneMatch(c -> c.getId().equals(id))) {
            return false;
        }
        repository.updateById(id, categoria);
        return true;
    }
}
