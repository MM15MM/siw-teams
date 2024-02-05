package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.repository.PlayerRepository;
import it.uniroma3.siw.service.PlayerService;

@Component
public class PlayerValidator implements Validator {
	
	@Autowired
	private PlayerService playerRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Player player = (Player)o;
		
		String nome = player.getName().trim();
        String cognome = player.getSurname().trim();
		
		
		 if (nome.isEmpty())
	            errors.rejectValue("name", "required");
	

	        if (cognome.isEmpty())
	            errors.rejectValue("surname", "required");
	       
	       /* else if (playerRepository. existsByNameAndSurname(nome,cognome))

	            errors.rejectValue("name", "duplicate");
	        errors.rejectValue("surname", "duplicate");*/

	    }

	
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Player.class.equals(aClass);
}
}
