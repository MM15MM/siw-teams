package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.President;

public interface PresidentRepository extends CrudRepository<President, Long> {
	
	public boolean existsByFiscalCode(String fiscalCode);

	
	public President findById(String id);

	public List<President> findBySport(String sport);


	public President findByCode(String code);
	


	public boolean existsByCode(String code);
}
