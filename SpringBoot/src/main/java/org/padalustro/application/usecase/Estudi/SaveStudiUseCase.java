package org.padalustro.application.usecase.Estudi;

import org.padalustro.domain.repository.EstudiRepository;
import org.padalustro.infrastructure.DTO.EstudiDTO;
import org.springframework.stereotype.Component;

@Component
public class SaveStudiUseCase {

    private final EstudiRepository repository;

    public SaveStudiUseCase(EstudiRepository repository) {
        this.repository = repository;
    }

    public void execute(EstudiDTO estudi) {
        repository.save(estudi);
    }
}
