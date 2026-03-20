package org.padalustro.application.usecase.Cataleg;

import org.padalustro.domain.exceptions.VideoCatalegNotFoundException;
import org.padalustro.domain.repository.VideoCatalegRepository;
import org.padalustro.infrastructure.DTO.VideoCatalegDTO;
import org.springframework.stereotype.Component;

@Component
public class GetVideoCatalegByIdUseCase {

    private final VideoCatalegRepository repository;

    public GetVideoCatalegByIdUseCase(VideoCatalegRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca un VideoCataleg per id.
     * Retorna el DTO si existeix, si no llança una excepció.
     */
    public VideoCatalegDTO execute(Long id) {
        return repository.findByIdDTO(id)
                .orElseThrow(() -> new VideoCatalegNotFoundException(id));
    }
}
