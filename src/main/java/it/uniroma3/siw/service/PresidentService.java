package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.President;
import it.uniroma3.siw.repository.PresidentRepository;

@Service
public class PresidentService {
	
	
	@Autowired
	private PresidentRepository presidentRepository;

	@Transactional
	public void save(President president) {
		// TODO Auto-generated method stub
		this.presidentRepository.save(president);
	}


}
