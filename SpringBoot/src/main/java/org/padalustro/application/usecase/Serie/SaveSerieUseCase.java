package org.padalustro.application.usecase.Serie;

import org.padalustro.domain.repository.SerieRepository;
import org.padalustro.infrastructure.DTO.SerieDTO;
import org.springframework.stereotype.Component;

@Component
public class SaveSerieUseCase {

    private final SerieRepository repository;

    public SaveSerieUseCase(SerieRepository repository) {
        this.repository = repository;
    }

    public void execute(SerieDTO estudi) {
        repository.save(estudi);
    }
}
