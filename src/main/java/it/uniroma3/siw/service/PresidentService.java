package it.uniroma3.siw.service;

import java.util.List;

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
  @Transactional
  public String getByFiscalCode(String fc) {
	  return this.presidentRepository.findByFiscalCode(fc);
  }
  @Transactional
  public boolean existByFiscalCode(String fc) {
	  return this.presidentRepository.existsByFiscalCode(fc);
  }
  
  @Transactional
public President findById(Long presidentId) {
	// TODO Auto-generated method stub
	return this.presidentRepository.findById(presidentId).get();
}
  @Transactional
public List<President> findAll() {
	// TODO Auto-generated method stub
	return (List<President>)this.presidentRepository.findAll();
}
  @Transactional
public void deleteById(Long id) {
	// TODO Auto-generated method stub
	this.presidentRepository.deleteById(id);
}
public List<President> findPresidentsBySport(String sport) {
	// TODO Auto-generated method stub
	return this.presidentRepository.findBySport(sport);
}

}
