package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	public boolean existsByFiscalCode(String fiscalCode);

	
	//public User findByCredentialsUsername(String username);
}
