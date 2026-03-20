package org.padalustro.infrastructure.DTO.IDs;

import java.io.Serializable;

import lombok.Data;

@Data
public class SerieIdDTO implements Serializable {
    private Long id;

    public SerieIdDTO() {}

    public SerieIdDTO(Long id) {
        this.id = id;
    }
}
