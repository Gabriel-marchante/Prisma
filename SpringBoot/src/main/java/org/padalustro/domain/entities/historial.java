package org.padalustro.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(
    name = "historial",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"perfil_id", "video_id"})
    }
)
public class historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long id;

    // FK a perfil
    @ManyToOne(optional = false)
    @JoinColumn(name = "perfil_id", nullable = false)
    private perfil perfil;

    // FK a usuari
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuari_id", nullable = false)
    private usuari usuari;

    // FK a video_cataleg
    @ManyToOne(optional = false)
    @JoinColumn(name = "video_id", nullable = false)
    private video_cataleg videoCataleg;

    @Column(nullable = false)
    private Integer visualization;

    @Column(name = "latest_play", nullable = false)
    private Integer latest_play;

    @Column(nullable = false)
    private Boolean completat;
}
