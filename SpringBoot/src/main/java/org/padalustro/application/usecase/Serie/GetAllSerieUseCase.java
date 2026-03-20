package org.padalustro.application.usecase.Serie;

import java.util.List;

import org.padalustro.domain.repository.SerieRepository;
import org.padalustro.infrastructure.DTO.SerieDTO;
import org.springframework.stereotype.Service;

@Service
public class GetAllSerieUseCase {

    private final SerieRepository SerieRepository;

    public GetAllSerieUseCase(SerieRepository SerieRepository) {
        this.SerieRepository = SerieRepository;
    }

    public List<SerieDTO> execute() {
        return SerieRepository.findAllDTO();
    }
}
