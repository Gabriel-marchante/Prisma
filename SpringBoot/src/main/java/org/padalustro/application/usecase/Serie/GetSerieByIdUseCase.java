package org.padalustro.application.usecase.Serie;

import org.padalustro.domain.exceptions.VideoCatalegNotFoundException;
import org.padalustro.domain.repository.SerieRepository;
import org.padalustro.infrastructure.DTO.SerieDTO;
import org.springframework.stereotype.Component;

@Component
public class GetSerieByIdUseCase {

    private final SerieRepository repository;

    public GetSerieByIdUseCase(SerieRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca un VideoCataleg per id.
     * Retorna el DTO si existeix, si no llança una excepció.
     */
    public SerieDTO execute(Long id) {
        return repository.findByIdDTO(id)
                .orElseThrow(() -> new VideoCatalegNotFoundException(id));
    }
}
