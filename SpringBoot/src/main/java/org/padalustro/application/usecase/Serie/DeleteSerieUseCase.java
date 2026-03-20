package org.padalustro.application.usecase.Serie;

import org.padalustro.domain.repository.SerieRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteSerieUseCase {

    private final SerieRepository repository;

    public DeleteSerieUseCase(SerieRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        repository.deleteById(id);
    }
}
