package org.padalustro.application.usecase.Category;

import java.util.List;

import org.padalustro.domain.repository.CategoriaRepository;
import org.padalustro.infrastructure.DTO.CategoriaDTO;
import org.springframework.stereotype.Component;


@Component
public class GetAllCategoryUseCase {

    private final CategoriaRepository repository;

    public GetAllCategoryUseCase(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<CategoriaDTO> execute() {
        return repository.findAllDTO();
    }
}

