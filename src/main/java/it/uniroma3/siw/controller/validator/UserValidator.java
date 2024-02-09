package it.uniroma3.siw.controller.validator;

import org.springframework.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.UserService;

@Component
public class UserValidator implements Validator {

	    final Integer FISCAL_CODE=16;
	    
	    @Autowired
	    private UserService userService;

	    @Override
	    public void validate(Object o, Errors errors) {
	        User user = (User) o;
	        String code = user.getCode().trim();
	        String fiscalCode = user.getFiscalCode().trim();



	        if (code.isEmpty())
	            errors.rejectValue("code", "required");
	        else if (this.userService.existsByCode(code) == true)
	            errors.rejectValue("code", "duplicate");
	       
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
