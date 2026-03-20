package org.padalustro.domain.repository;

import java.util.List;
import java.util.Optional;

import org.padalustro.infrastructure.DTO.CategoriaDTO;
import org.padalustro.infrastructure.DTO.EstudiDTO;



public interface EstudiRepository {


    List<EstudiDTO> listAllEstudis();
    List<EstudiDTO> findAllDTO();
    void saveAll(List<EstudiDTO> estudis);
    List<EstudiDTO> findByType(String type);




    void save(EstudiDTO estudi);
    void updateById(Long id, EstudiDTO dto);
    void deleteById(Long id);

    Optional<EstudiDTO> findByIdDTO(Long id);


}
