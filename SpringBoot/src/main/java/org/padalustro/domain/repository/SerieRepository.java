package org.padalustro.domain.repository;

import java.util.List;
import java.util.Optional;

import org.padalustro.infrastructure.DTO.SerieDTO;




public interface SerieRepository {


    List<SerieDTO> listAllSeries();
    List<SerieDTO> findAllDTO();
    void saveAll(List<SerieDTO> serie);
    List<SerieDTO> findByType(String type);


    void save(SerieDTO serie);
    void updateById(Long id, SerieDTO dto);
    void deleteById(Long id);

    Optional<SerieDTO> findByIdDTO(Long id);

}
