package org.padalustro.application.usecase.Cataleg;

import org.padalustro.domain.exceptions.VideoCatalegNotFoundException;
import org.padalustro.domain.repository.VideoCatalegRepository;
import org.springframework.stereotype.Component;


@Component
public class DeleteVideoCatalegUseCase {

    private final VideoCatalegRepository repository;

    public DeleteVideoCatalegUseCase(VideoCatalegRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id) {

        if (!repository.existsById(id)) {
            throw new VideoCatalegNotFoundException(id);
        }

        repository.deleteById(id);
    }
}
