package org.padalustro.application.usecase.Category;

import org.padalustro.domain.repository.CategoriaRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteCategoryUseCase {

    private final CategoriaRepository repository;

    public DeleteCategoryUseCase(CategoriaRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        repository.deleteById(id);
    }
}
