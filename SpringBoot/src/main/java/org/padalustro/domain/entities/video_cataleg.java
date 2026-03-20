package org.padalustro.domain.entities;

import java.sql.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

// many to one serie
// many to one estudi
// many to many categoria



// TO-DO
// eliminar  thumnail
// Còdec: N/A
// Resolució: N/A
// Pes: N/A

@Data
@Entity
@Table(name = "video_cataleg")
public class video_cataleg {

    @Id
    @Column(name = "id_video_cataleg")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;


    @ManyToMany
    @JoinTable(name = "video_categoria", joinColumns = @JoinColumn(name = "id_video_cataleg"), inverseJoinColumns = @JoinColumn(name = "categoria_id"))
    private Set<categoria> category;

    @ManyToOne
    @JoinColumn(name = "estudi_id", nullable = false)
    private estudi study;

    @Column(nullable = true)
    private double rating;

    @Column(nullable = true)
    private Integer season;

    @ManyToOne
    @JoinColumn(name = "serie_id", nullable = true)
    private serie series;

    @Column(nullable = false)
    private Integer chapter;

    @Column(name = "date_emission", nullable = false)
    private Date date_emission = new Date(System.currentTimeMillis());

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private String codec;

    @Column(nullable = false)
    private String resolucio;

    @Column(nullable = false)
    private long  pes;

    public video_cataleg() {
    }

    public video_cataleg(String title, String description,
            Set<categoria> category,
            estudi study,
            double rating, Integer season, serie series, Integer chapter, Date date_emission,
            Integer duration,String codec, String resolucio, long pes) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.study = study;
        this.rating = rating;
        this.season = season;
        this.series = series;
        this.chapter = chapter;
        if (date_emission != null) {
            this.date_emission = date_emission;
        }

        this.duration = duration;
        this.codec = codec;
        this.resolucio = resolucio;
        this.pes = pes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<categoria> getCategory() {
        return category;
    }

    public void setCategory(Set<categoria> category) {
        this.category = category;
    }

    public estudi getStudy() {
        return study;
    }

    public void setStudy(estudi study) {
        this.study = study;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public serie getSeries() {
        return series;
    }

    public void setSeries(serie series) {
        this.series = series;
    }

    public Integer getChapter() {
        return chapter;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public Date getDateEmission() {
        return date_emission;
    }

    public void setDateEmission(Date date_emission) {
        this.date_emission = date_emission;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getResolucio() {
        return resolucio;
    }

    public void setResolucio(String resolucio) {
        this.resolucio = resolucio;
    }

    public long getPes() {
        return pes;
    }

    public void setPes(long pes) {
        this.pes = pes;
    }


}