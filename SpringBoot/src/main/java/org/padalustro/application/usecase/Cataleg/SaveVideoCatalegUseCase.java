package org.padalustro.application.usecase.Cataleg;

import org.padalustro.domain.repository.VideoCatalegRepository;
import org.padalustro.infrastructure.DTO.VideoCatalegDTO;
import org.springframework.stereotype.Service;

@Service
public class SaveVideoCatalegUseCase {

    private final VideoCatalegRepository videoCatalegRepository;

    public SaveVideoCatalegUseCase(VideoCatalegRepository videoCatalegRepository) {
        this.videoCatalegRepository = videoCatalegRepository;
    }

    public void execute(VideoCatalegDTO dto) {
        videoCatalegRepository.saveVideoCataleg(dto);
    }
}
