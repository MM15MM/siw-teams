package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.service.TeamService;

@Component
public class TeamValidator implements Validator {

		@Autowired
		private TeamService teamService;

		@Override
		public void validate(Object o, Errors errors) {
			Team team = (Team)o;
	        String nome = team.getName().trim();
	        if (nome.isEmpty())
	            errors.rejectValue("name", "required");
	        else if (teamService.existsByNameAndYear(nome,  team.getYear())) {
				errors.rejectValue("name", "duplicate");
			}
		}
		@Override
		public boolean supports(Class<?> aClass) {
			return Team.class.equals(aClass);
	}
}
