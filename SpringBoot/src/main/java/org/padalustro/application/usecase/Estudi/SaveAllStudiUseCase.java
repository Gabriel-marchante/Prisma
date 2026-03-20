package org.padalustro.application.usecase.Estudi;

import java.util.List;

import org.padalustro.domain.repository.EstudiRepository;
import org.padalustro.infrastructure.DTO.EstudiDTO;
import org.springframework.stereotype.Component;

@Component
public class SaveAllStudiUseCase {

    private final EstudiRepository estudiRepository;

    public SaveAllStudiUseCase(EstudiRepository estudiRepository) {
        this.estudiRepository = estudiRepository;
    }

    public void execute(List<EstudiDTO> estudis) {
        estudiRepository.saveAll(estudis);
    }
}
