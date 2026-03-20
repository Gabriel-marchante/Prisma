package org.padalustro.domain.repository;

import java.util.List;
import java.util.Optional;

import org.padalustro.domain.entities.video_cataleg;
import org.padalustro.infrastructure.DTO.VideoCatalegDTO;


public interface VideoCatalegRepository {

    void saveVideoCataleg(VideoCatalegDTO videoCatalegDTO);

    VideoCatalegDTO getVideoCatalegById(Long id);

    List<VideoCatalegDTO> listAllVideoCataleg();

    boolean deleteVideoCataleg(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<VideoCatalegDTO> findAllDTO();

    Optional<VideoCatalegDTO> findByIdDTO(Long id);

    Optional<video_cataleg> findById(Long id);

    void delete(video_cataleg video_cataleg);

    void updateVideoCataleg(Long id, VideoCatalegDTO dto);
    
}


