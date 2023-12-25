package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {
	
	public boolean existsByNameAndYear(String name, int year);

	public List<Team> findByName(String name);
}
