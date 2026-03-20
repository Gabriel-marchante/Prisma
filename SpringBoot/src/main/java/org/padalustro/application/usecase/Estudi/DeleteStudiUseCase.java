package org.padalustro.application.usecase.Estudi;

import org.padalustro.domain.repository.EstudiRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteStudiUseCase {

    private final EstudiRepository repository;

    public DeleteStudiUseCase(EstudiRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        repository.deleteById(id);
    }
}
