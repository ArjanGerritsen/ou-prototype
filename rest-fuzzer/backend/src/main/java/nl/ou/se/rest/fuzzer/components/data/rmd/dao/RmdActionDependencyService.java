package nl.ou.se.rest.fuzzer.components.data.rmd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import nl.ou.se.rest.fuzzer.components.data.rmd.domain.RmdActionDependency;

public interface RmdActionDependencyService extends CrudRepository<RmdActionDependency, Long> {

    @Query(value = "SELECT DISTINCT d FROM rmd_actions_dependencies d LEFT JOIN FETCH d.action a LEFT JOIN FETCH a.sut WHERE a.sut.id = :sutId")
    List<RmdActionDependency> findBySutId(Long sutId);

}