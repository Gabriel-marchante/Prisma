package org.padalustro.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "valoracio_video")
public class valoracio_video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK a perfil
    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private perfil perfil;

    // FK a video
    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    private video_cataleg video;

    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "rating", nullable = false)
    private Integer rating;
}
