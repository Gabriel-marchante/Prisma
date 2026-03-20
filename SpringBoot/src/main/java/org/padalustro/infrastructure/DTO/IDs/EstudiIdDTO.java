package org.padalustro.infrastructure.DTO.IDs;

import java.io.Serializable;

import lombok.Data;

@Data
public class EstudiIdDTO implements Serializable {
    private Long id;

    public EstudiIdDTO() {}

    public EstudiIdDTO(Long id) {
        this.id = id;
    }
}
