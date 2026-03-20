package org.padalustro.application.usecase.Category;

import java.util.List;

import org.padalustro.domain.repository.CategoriaRepository;
import org.padalustro.infrastructure.DTO.CategoriaDTO;
import org.springframework.stereotype.Component;

@Component
public class SaveAllCategoryUseCase {

    private final CategoriaRepository categoryRepository;

    public SaveAllCategoryUseCase(CategoriaRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void execute(List<CategoriaDTO> categories) {
        categoryRepository.saveAll(categories);
    }
}
