package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.President;
import it.uniroma3.siw.repository.PresidentRepository;

@Component
public class PresidentValidator implements Validator{

	@Autowired
	private PresidentRepository presidentRepository;

	@Override
	public void validate(Object o, Errors errors) {
		President president = (President)o;
		if (president.getName()!=null && president.getSurname()!=null 
				&& presidentRepository.existsByFiscalCode(president.getFiscalCode())) {
			errors.reject("president.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return President.class.equals(aClass);
}
}
