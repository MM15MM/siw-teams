package it.uniroma3.siw.controller.validator;

import org.springframework.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.UserService;

@Component
public class UserValidator implements Validator {
	    
	    final Integer MAX_NAME_LENGTH = 100;
	    final Integer MIN_NAME_LENGTH = 2;
	    final Integer FISCAL_CODE=16;
	    
	    @Autowired
	    private UserService userService;

	    @Override
	    public void validate(Object o, Errors errors) {
	        User user = (User) o;
	        String nome = user.getName().trim();
	        String cognome = user.getSurname().trim();
	        String fiscalCode = user.getFiscalCode().trim();

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
	        else  if (this.userService.getFiscalCode(fiscalCode) == true)
	            errors.rejectValue("fiscalCode", "duplicate");

	    }

	    @Override
	    public boolean supports(Class<?> clazz) {
	        return User.class.equals(clazz);
	    }
}
