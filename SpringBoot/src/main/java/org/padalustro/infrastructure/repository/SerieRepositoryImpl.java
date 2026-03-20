package org.padalustro.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.padalustro.domain.entities.serie;
import org.padalustro.domain.repository.SerieRepository;
import org.padalustro.infrastructure.DTO.SerieDTO;
import org.padalustro.infrastructure.repository.jpa.SerieJpaRepository;
import org.springframework.stereotype.Service;
@Service
public class SerieRepositoryImpl implements SerieRepository {

    private final SerieJpaRepository jpaRepository;

    public SerieRepositoryImpl(SerieJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<SerieDTO> listAllSeries() {
        return jpaRepository.findAll()
                .stream()
                .map(SerieDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SerieDTO> findAllDTO() {
        return listAllSeries();
    }

    
    @Override
    public List<SerieDTO> findByType(String type) {
        return jpaRepository.findByNameIgnoreCase(type)
            .stream()
            .map(SerieDTO::convertToDTO)
            .toList();
    }

    @Override
    public void saveAll(List<SerieDTO> series) {
        List<serie> entities = series.stream()
                .map(SerieDTO::toEntity)
                .collect(Collectors.toList());
        jpaRepository.saveAll(entities);
    }

    @Override
    public void save(SerieDTO serieDTO) {
        jpaRepository.save(serieDTO.toEntity());
    }

    @Override
    public void updateById(Long id, SerieDTO dto) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setName(dto.getName());
            entity.setClassification(dto.getClassification());
            jpaRepository.save(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }


    @Override
    public Optional<SerieDTO> findByIdDTO(Long id) {
        return jpaRepository.findById(id).map(SerieDTO::convertToDTO);
    }


}
