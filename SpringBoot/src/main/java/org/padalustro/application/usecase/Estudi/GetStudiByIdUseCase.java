package org.padalustro.application.usecase.Estudi;

import org.padalustro.domain.exceptions.VideoCatalegNotFoundException;
import org.padalustro.domain.repository.EstudiRepository;
import org.padalustro.infrastructure.DTO.EstudiDTO;
import org.springframework.stereotype.Component;

@Component
public class GetStudiByIdUseCase {

    private final EstudiRepository repository;

    public GetStudiByIdUseCase(EstudiRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca un VideoCataleg per id.
     * Retorna el DTO si existeix, si no llança una excepció.
     */
    public EstudiDTO execute(Long id) {
        return repository.findByIdDTO(id)
                .orElseThrow(() -> new VideoCatalegNotFoundException(id));
    }
}
