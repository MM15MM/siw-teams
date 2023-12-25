package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.President;

public interface PresidentRepository extends CrudRepository<President, Long> {
	
	public boolean existsByFiscalCode(String fiscalCode);
}
