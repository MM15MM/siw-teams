package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.repository.PlayerRepository;

@Component
public class PlayerValidator implements Validator {
	
	@Autowired
	private PlayerRepository playerRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Player player = (Player)o;
		
		if (player.getName()!=null && player.getSurname()!=null 
				&& playerRepository.existsByNameAndSurname(player.getName(),  player.getSurname())) {
			errors.reject("player.duplicate");
		}

	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Player.class.equals(aClass);
}
}
