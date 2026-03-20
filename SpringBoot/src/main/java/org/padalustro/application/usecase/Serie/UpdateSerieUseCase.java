package org.padalustro.application.usecase.Serie;

import org.padalustro.domain.repository.SerieRepository;
import org.padalustro.infrastructure.DTO.SerieDTO;
import org.springframework.stereotype.Component;

@Component
public class UpdateSerieUseCase {

    private final SerieRepository repository;

    public UpdateSerieUseCase(SerieRepository repository) {
        this.repository = repository;
    }

    public boolean execute(Long id, SerieDTO serie) {
        if (repository.findAllDTO().stream().noneMatch(c -> c.getId().equals(id))) {
            return false;
        }
        repository.updateById(id, serie);
        return true;
    }
}
