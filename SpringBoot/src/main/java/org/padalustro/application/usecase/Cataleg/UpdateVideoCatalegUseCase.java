package org.padalustro.application.usecase.Cataleg;

import org.padalustro.domain.repository.VideoCatalegRepository;
import org.padalustro.infrastructure.DTO.VideoCatalegDTO;
import org.springframework.stereotype.Service;

@Service
public class UpdateVideoCatalegUseCase {

    private final VideoCatalegRepository videoCatalegRepository;

    public UpdateVideoCatalegUseCase(VideoCatalegRepository videoCatalegRepository) {
        this.videoCatalegRepository = videoCatalegRepository;
    }

    public boolean execute(Long id, VideoCatalegDTO dto) {
        if (!videoCatalegRepository.existsById(id)) {
            return false;
        }

        videoCatalegRepository.updateVideoCataleg(id, dto);
        return true;
    }
}
