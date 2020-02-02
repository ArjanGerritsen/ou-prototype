package nl.ou.se.rest.fuzzer.service.sut.domain;

import nl.ou.se.rest.fuzzer.data.rmd.domain.ParameterContext;
import nl.ou.se.rest.fuzzer.data.rmd.domain.ParameterType;

public class ParameterDto {

    // variables
    private Long id;
    private Integer position;
    private String name;
    private Boolean required;
    private String description;
    private ParameterType type;
    private ParameterContext context;
    private String metaDataTuplesJson;

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ParameterType getType() {
        return type;
    }

    public void setType(ParameterType type) {
        this.type = type;
    }

    public ParameterContext getContext() {
        return context;
    }

    public void setContext(ParameterContext context) {
        this.context = context;
    }

    public String getMetaDataTuplesJson() {
        return metaDataTuplesJson;
    }

    public void setMetaDataTuplesJson(String metaDataTuplesJson) {
        this.metaDataTuplesJson = metaDataTuplesJson;
    }
}
