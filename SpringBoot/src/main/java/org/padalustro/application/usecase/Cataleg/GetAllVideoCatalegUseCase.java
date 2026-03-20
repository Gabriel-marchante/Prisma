package org.padalustro.application.usecase.Cataleg;

import java.util.List;

import org.padalustro.domain.repository.VideoCatalegRepository;
import org.padalustro.infrastructure.DTO.VideoCatalegDTO;
import org.springframework.stereotype.Component;


@Component
public class GetAllVideoCatalegUseCase {

    private final VideoCatalegRepository repository;

    public GetAllVideoCatalegUseCase(VideoCatalegRepository repository) {
        this.repository = repository;
    }

    public List<VideoCatalegDTO> execute() {
        return repository.findAllDTO();
    }
}

