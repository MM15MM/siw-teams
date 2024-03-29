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
public List<President> findPresidentsBySport(String sport) {
	// TODO Auto-generated method stub
	return this.presidentRepository.findBySport(sport);
}
@Transactional
public President findByCode(String code) {
	// TODO Auto-generated method stub
	return this.presidentRepository.findByCode(code);
}

@Transactional
public boolean existByCode(String code) {
	// TODO Auto-generated method stub
	return this.presidentRepository.existsByCode(code);
}
@Transactional
public boolean existsByFiscalCodeAndCode(String fiscalCode, String code) {
	// TODO Auto-generated method stub
	return this.presidentRepository.existsByFiscalCodeAndCode(fiscalCode,code);
}

}
