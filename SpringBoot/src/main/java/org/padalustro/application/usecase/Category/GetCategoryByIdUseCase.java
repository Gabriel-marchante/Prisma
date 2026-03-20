package org.padalustro.application.usecase.Category;

import org.padalustro.domain.exceptions.VideoCatalegNotFoundException;
import org.padalustro.domain.repository.CategoriaRepository;
import org.padalustro.infrastructure.DTO.CategoriaDTO;
import org.springframework.stereotype.Component;

@Component
public class GetCategoryByIdUseCase {

    private final CategoriaRepository repository;

    public GetCategoryByIdUseCase(CategoriaRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca un VideoCataleg per id.
     * Retorna el DTO si existeix, si no llança una excepció.
     */
    public CategoriaDTO execute(Long id) {
        return repository.findByIdDTO(id)
                .orElseThrow(() -> new VideoCatalegNotFoundException(id));
    }
}
