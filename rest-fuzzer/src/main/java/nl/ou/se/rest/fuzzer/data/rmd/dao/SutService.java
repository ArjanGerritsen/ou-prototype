package nl.ou.se.rest.fuzzer.data.rmd.dao;

import org.springframework.data.repository.CrudRepository;

import nl.ou.se.rest.fuzzer.data.rmd.domain.Sut;

public interface SutService extends CrudRepository<Sut, Long> {

}