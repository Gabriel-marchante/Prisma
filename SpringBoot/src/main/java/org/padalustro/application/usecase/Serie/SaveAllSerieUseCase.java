package org.padalustro.application.usecase.Serie;

import java.util.List;

import org.padalustro.domain.repository.SerieRepository;
import org.padalustro.infrastructure.DTO.SerieDTO;
import org.springframework.stereotype.Component;

@Component
public class SaveAllSerieUseCase {

    private final SerieRepository serieRepository;

    public SaveAllSerieUseCase(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void execute(List<SerieDTO> series) {
        serieRepository.saveAll(series);
    }
}
