package org.padalustro.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.padalustro.domain.entities.categoria;
import org.padalustro.domain.entities.estudi;
import org.padalustro.domain.repository.EstudiRepository;
import org.padalustro.infrastructure.DTO.CategoriaDTO;
import org.padalustro.infrastructure.DTO.EstudiDTO;
import org.padalustro.infrastructure.repository.jpa.EstudiJpaRepository;
import org.springframework.stereotype.Service;
@Service
public class EstudiRepositoryImpl implements EstudiRepository {
    
    private final EstudiJpaRepository jpaRepository;

    public EstudiRepositoryImpl(EstudiJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<EstudiDTO> listAllEstudis() {
        return jpaRepository.findAll()
                .stream()
                .map(EstudiDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EstudiDTO> findAllDTO() {
        return listAllEstudis();
    }

    
    @Override
    public List<EstudiDTO> findByType(String type) {
        return jpaRepository.findByNameIgnoreCase(type)
            .stream()
            .map(EstudiDTO::convertToDTO)
            .toList();
    }

    @Override
    public void saveAll(List<EstudiDTO> estudis) {
        List<estudi> entities = estudis.stream()
                .map(EstudiDTO::toEntity)
                .collect(Collectors.toList());
        jpaRepository.saveAll(entities);
    }

    @Override
    public void save(EstudiDTO estudiDTO) {
        jpaRepository.save(estudiDTO.toEntity());
    }

    @Override
    public void updateById(Long id, EstudiDTO dto) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setName(dto.getName()); 
            jpaRepository.save(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<EstudiDTO> findByIdDTO(Long id) {
        return jpaRepository.findById(id).map(EstudiDTO::convertToDTO);
    }


}