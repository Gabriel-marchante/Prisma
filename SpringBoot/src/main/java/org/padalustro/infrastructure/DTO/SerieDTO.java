package org.padalustro.infrastructure.DTO;

import java.io.Serializable;

import org.padalustro.domain.entities.serie;

import lombok.Data;

@Data
public class SerieDTO implements Serializable {

    private Long id;
    private String name;
    private int classification;

    public SerieDTO() {}

    public SerieDTO(Long id, String name, int classification) {
        this.id = id;
        this.name = name;
        this.classification = classification;
    }

    public static SerieDTO convertToDTO(serie serie) {
        SerieDTO dto = new SerieDTO();
        dto.setId(serie.getId());
        dto.setName(serie.getName());
        dto.setClassification(serie.getClassification());
        return dto;
    }

    public serie toEntity() {
        serie serie = new serie();
        serie.setId(this.id);
        serie.setName(this.name);
        serie.setClassification(this.classification);
        return serie;
    }
}
