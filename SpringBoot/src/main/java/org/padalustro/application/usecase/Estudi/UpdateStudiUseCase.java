package org.padalustro.application.usecase.Estudi;

import org.padalustro.domain.repository.EstudiRepository;
import org.padalustro.infrastructure.DTO.EstudiDTO;
import org.springframework.stereotype.Component;

@Component
public class UpdateStudiUseCase {

    private final EstudiRepository repository;

    public UpdateStudiUseCase(EstudiRepository repository) {
        this.repository = repository;
    }

    public boolean execute(Long id, EstudiDTO estudi) {
        if (repository.findAllDTO().stream().noneMatch(c -> c.getId().equals(id))) {
            return false;
        }
        repository.updateById(id, estudi);
        return true;
    }
}
