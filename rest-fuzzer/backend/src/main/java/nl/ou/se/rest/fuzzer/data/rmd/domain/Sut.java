package nl.ou.se.rest.fuzzer.data.rmd.domain;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.SortNatural;

@Entity(name = "rmd_suts")
public class Sut {

    // variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String location;

    @Size(max = 64)
    private String title;

    @Size(max = 128)
    private String description;

    @Size(max = 255)
    private String host;

    @Size(max = 255)
    private String basePath;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sut_id")
    @SortNatural
    private SortedSet<Action> actions = new TreeSet<>();

    // constructors
    public Sut() {
    }

    public Sut(String location) {
        this.location = location;
    }

    // methods
    public void addAction(Action action) {
        action.setSut(this);
        this.actions.add(action);
    }

    // getters and setters
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public SortedSet<Action> getActions() {
        return actions;
    }

    public void setActions(SortedSet<Action> actions) {
        this.actions = actions;
    }

    // toString
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}