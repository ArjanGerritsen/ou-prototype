package nl.ou.se.rest.fuzzer.components.service.fuz.domain;

import java.time.LocalDateTime;

import nl.ou.se.rest.fuzzer.components.data.fuz.domain.FuzType;
import nl.ou.se.rest.fuzzer.components.service.rmd.domain.RmdSutDto;

public class FuzProjectDto {

    // variables
    private Long id;
    private FuzType type;
    private String metaDataTuplesJson;
    private RmdSutDto sut;
    private LocalDateTime createdAt;

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetaDataTuplesJson() {
        return metaDataTuplesJson;
    }

    public void setMetaDataTuplesJson(String metaDataTuplesJson) {
        this.metaDataTuplesJson = metaDataTuplesJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public FuzType getType() {
        return type;
    }

    public void setType(FuzType type) {
        this.type = type;
    }

    public RmdSutDto getSut() {
        return sut;
    }

    public void setSut(RmdSutDto sut) {
        this.sut = sut;
    }
}