package it.uniroma3.siw.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.TeamValidator;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.TeamService;

@Controller
public class TeamController {
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private TeamValidator teamValidator;
	
	@Autowired
	private CredentialsService credentialsService;
	

	/*----------------------------------------------------*/
	/*----------------------------------------------------*/
	/*--------------------ADMIN---------------------------*/
	/*----------------------------------------------------*/
	/*----------------------------------------------------*/
	/*----------------------------------------------------*/
		
		
		/*--------------PERCORSO PER L'ADMIN---------------*/
		@GetMapping(value ="/admin/teams")
		public String getTeamsAdmin(Model model) {
			model.addAttribute("teams", this.teamService.findAll());
			return "admin/teams.html";
		}

		@GetMapping(value ="/admin/team/{id}")
		public String getTeamAdmin(@PathVariable("id") Long id, Model model) {

			Team team = this.teamService.findById(id);
			model.addAttribute("team", team);
			return "admin/teamAdmin.html";
		}
		
		
		/*------------------AGGIUNTA NUOVO COMIC-------------------*/
		
		@GetMapping(value = "/admin/formNewTeam")
		public String formNewTeam(Model model) {
			model.addAttribute("team", new Team());
			return "admin/formNewTeam.html";
		}

		@PostMapping(value = "/admin/team")
		public String newTeam(@Valid @ModelAttribute("team") Team team, 
				              BindingResult bindingResult, Model model) {
			this.teamValidator.validate(team, bindingResult);
			if(!bindingResult.hasErrors()) { //se i dati sono corretti
			    this.teamService.save(team); //salvo oggetto team  
				model.addAttribute("team", team);
				return "redirect:/admin/teams"; 
			} 
			else {
				return "admin/error.html"; 
			}
		}
		
		/*----------ADMIN ELIMINA TEAM----------*/
		
		@PostMapping(value = "/admin/deleteTeam/{id}")
		public String deleteTeam(@PathVariable("id") Long id, Model model){

			Team team = this.teamService.findById(id);
			this.teamService.deleteById(team.getId());

			return "redirect:/admin/teams";
		}
		
		/*---------ADMIN MODIFICA DETTAGLI TEAM--------*/
		
		@GetMapping(value = "/admin/formUpdateTeam/{id}")
		public String editTeam(@PathVariable("id") Long id, Model model) {
			model.addAttribute("team", this.teamService.findById(id));
			return "admin/formUpdateTeam.html";
		}

		@PostMapping(value = "/admin/formUpdateTeam/{id}")
		public String editingTeam(@Valid @PathVariable("id") Long id, @ModelAttribute("team") Team team,
				BindingResult BindingResult, Model model) {
			Team t = this.teamService.findById(id);
			t.setName(team.getName());
			t.setAddress(team.getAddress());
			t.setPresident(team.getPresident());
			t.setYear(team.getYear());
			t.setSport(team.getSport());
			t.setImage(team.getImage());

			this.teamValidator.validate(t, BindingResult);
			if (!BindingResult.hasErrors()) {
				List<Team> teams = this.teamService.findAll();
				model.addAttribute("teams", teams);
				this.teamService.save(t);
				return "admin/teams.html";
			}
			model.addAttribute("team", this.teamService.findById(id));
			return "admin/formUpdateTeam.html";
		}
		
	/*---------RICERCA TEAMS DA PARTE DELL'ADMIN----------*/
		


		@RequestMapping(value="/admin/formSearchTeams",method = RequestMethod.GET)
		public String formSearchTeamsAdmin() {
			return "admin/formSearchTeams.html";
		}
		@PostMapping(value ="/admin/searchTeams")
		public String searchTeamsAdmin(Model model, @RequestParam String name) {
			model.addAttribute("teams", this.teamService.findByName(name));
			return "admin/adminTeamsFound.html";

		}
		
		/*----------------------------------------------------*/
		/*----------------------------------------------------*/
		/*--------------------GENERAL-------------------------*/
		/*----------------------------------------------------*/
		/*----------------------------------------------------*/
		/*----------------------------------------------------*/
		
		/*CHIUNQUE VISUALIZZA LISTA TEAMS E DETTAGLI TEAM*/
		
		@GetMapping( "/team/{id}")
		public String getTeam(@PathVariable("id") Long id, Model model, Principal principal) {
			model.addAttribute("team", this.teamService.findById(id));
			if(principal!=null && isPrincipalPresidentOfTeam(principal, id)) {
			return "team.html";
			}
			else {
				return "teamDefaultUser.html";
			}
		}


		@PostMapping(value = "/searchTeams")
		public String searchTeamsByName(Model model, @RequestParam String name) {
			model.addAttribute("teams", this.teamService.findByName(name));
			return "foundTeams.html";
		}
		
		@GetMapping(value = "/teams")
		public String showTeams(@RequestParam(name = "sport", required = false) String sport, Model model) {
	        List<Team> teams;

	        if (sport != null && !sport.isEmpty()) {
	            // Se lo sport è specificato, recupera solo le squadre di quel tipo
	            teams = this.teamService.findTeamsBySport(sport);
	        } else {
	            // Altrimenti, recupera tutte le squadre
	            teams = this.teamService.findAll();
	        }

	        model.addAttribute("teams", teams);
	        model.addAttribute("selectedSport", sport);

	        // Aggiungi tutti gli sport disponibili al model
	        List<String> sports = this.teamService.getSports();
	        model.addAttribute("sports", sports);

	        return "teams";
		}
		
		// Metodo per verificare se il principal è già presidente di una squadra specifica
		private boolean isPrincipalPresidentOfTeam(Principal principal, Long teamId) {
		    String username = principal.getName();
		    User user = this.credentialsService.getCredentials(username).getUser();

		    // Verifica se l'utente è presidente di una squadra e se quella squadra ha l'id specificato
		    return user.getPresident() != null && user.getPresident().getTeam() != null &&
		           user.getPresident().getTeam().getId().equals(teamId);
		}
}
