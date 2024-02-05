package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.PresidentRepository;
import it.uniroma3.siw.service.PresidentService;

@Component
public class PresidentValidator implements Validator{

	@Autowired
	private PresidentRepository presidentRepository;
	
	@Autowired
	private PresidentService presidentService;
	
	 final Integer MAX_NAME_LENGTH = 100;
	 final Integer MIN_NAME_LENGTH = 2;
	final Integer FISCAL_CODE=16;

	@Override
	public void validate(Object o, Errors errors) {
		President president = (President)o;
		
	        String nome = president.getName().trim();
	        String cognome = president.getSurname().trim();
	        String fiscalCode = president.getFiscalCode().trim();

	        if (nome.isEmpty())
	            errors.rejectValue("nome", "required");
	        else if (nome.length() < MIN_NAME_LENGTH || nome.length() > MAX_NAME_LENGTH)
	            errors.rejectValue("nome", "size");

	        if (cognome.isEmpty())
	            errors.rejectValue("cognome", "required");
	        else if (cognome.length() < MIN_NAME_LENGTH || cognome.length() > MAX_NAME_LENGTH)
	            errors.rejectValue("cognome", "size");
	       
	        if (fiscalCode.isEmpty())
	            errors.rejectValue("fiscalCode", "required");
	        else if (fiscalCode.length()!= FISCAL_CODE)
	            errors.rejectValue("fiscalCode", "size");
	        else  if (this.presidentService.existByFiscalCode(fiscalCode) == true)
	            errors.rejectValue("fiscalCode", "duplicate");

	    }
		
		
		
	
	@Override
	public boolean supports(Class<?> aClass) {
		return President.class.equals(aClass);
}
}
