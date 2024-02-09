package it.uniroma3.siw.controller;

import java.security.Principal;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.TeamValidator;
import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PresidentService;
import it.uniroma3.siw.service.TeamService;
import it.uniroma3.siw.service.UserService;

@Controller
public class TeamController {
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private TeamValidator teamValidator;
	
	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private PresidentService presidentService;

	@Autowired
	private UserService userService;
	

	/*----------------------------------------------------*/
	/*----------------------------------------------------*/
	/*--------------------ADMIN---------------------------*/
	/*----------------------------------------------------*/
	/*----------------------------------------------------*/
	/*----------------------------------------------------*/
		
		
		/*--------------PERCORSO PER L'ADMIN PER VISUALIZZARE LE/A SQUADRE/A---------------*/
		@GetMapping(value ="/admin/teams")
		public String getTeamsAdmin(@RequestParam(name = "sport", required = false) String sport, Model model) {
	        List<Team> teams;

	        if (sport != null && !sport.isEmpty()) {
	            // recupera  squadre di quel tipo
	            teams = this.teamService.findTeamsBySport(sport);
	        } else {
	            // Altrimenti, recupera tutte le squadre
	            teams = this.teamService.findAll();
	        }

	        model.addAttribute("teams", teams);
	        model.addAttribute("selectedSport", sport);

	        // Aggiungi tutti gli sport disponibili 
	        List<String> sports = this.teamService.getSports();
	        model.addAttribute("sports", sports);

			return "admin/teams.html";
		}

		@GetMapping(value ="/admin/team/{id}")
		public String getTeamAdmin(@PathVariable("id") Long id, Model model) {

			Team team = this.teamService.findById(id);
			List<President> presidents = this.presidentsToAdd(team);
		    
		    model.addAttribute("presidentsToAdd", presidents);
		    model.addAttribute("team", team);
		    
			return "admin/teamAdmin.html";
		}
		public List<President> presidentsToAdd(Team team){
			
			List<President> presidents = new ArrayList<President>();
			List<President> presidentsPresenti=(List<President>) this.presidentService.findAll(); 
			
			for(President p: presidentsPresenti) {
				
		if ((p.getTeam()==null && p.getSport().equals(team.getSport())))  {
					
					presidents.add(p);
				}
			}
			return presidents;
		}
		

		
		/*------------------AGGIUNTA NUOVO TEAM-------------------*/
		
		
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
			
				return "admin/formNewTeam"; 
			
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
			return "redirect:/admin/team/"+id;
		}
		
	/*---------RICERCA TEAMS DA PARTE DELL'ADMIN----------*/
		

		@PostMapping(value ="/admin/searchTeams")
		public String searchTeamsAdmin(Model model, @RequestParam String name) {
			model.addAttribute("teams", this.teamService.findByName(name));
			return "admin/adminTeamsFound.html";

		}
		
		/*----------------------------------------------------*/
		/*----------------------------------------------------*/
		/*---------------GENERALE E PRESIDENTE----------------*/
		/*----------------------------------------------------*/
		/*----------------------------------------------------*/
		/*----------------------------------------------------*/
		
		/*CHIUNQUE VISUALIZZA LISTA TEAMS, DETTAGLI TEAM E PUO' CERCARE LE SQUADRE*/
		


		@PostMapping(value = "/searchTeams")
		public String searchTeamsByName(Model model, @RequestParam String name) {
			model.addAttribute("teams", this.teamService.findByName(name));
			return "foundTeams.html";
		}
		
		@GetMapping(value = "/teams")
		public String showTeams(@RequestParam(name = "sport", required = false) String sport, Model model) {
	        List<Team> teams;

	        if (sport != null && !sport.isEmpty()) {
	            //  recupera solo le squadre di quel tipo
	            teams = this.teamService.findTeamsBySport(sport);
	        } else {
	            // Altrimenti, recupera tutte le squadre
	            teams = this.teamService.findAll();
	        }

	        model.addAttribute("teams", teams);
	        model.addAttribute("selectedSport", sport);

	        // Aggiungi tutti gli sport disponibili 
	        List<String> sports = this.teamService.getSports();
	        model.addAttribute("sports", sports);

	        return "teams";
		}
		
		
		
/* VISUALIZZAZIONE TEAM*/
		@GetMapping( value="/team/{id}")
		public String getTeam(@PathVariable("id") Long id, Model model, Principal principal) {
			Team team = this.teamService.findById(id);
		    model.addAttribute("team", team);

		    if (principal == null) { // se non si è autenticati restituisce pagina default
		        return "teamDefaultUser.html";
		    }

		    String username = principal.getName();
		    User user = this.credentialsService.getCredentials(username).getUser();

		    if (user == null) {
		        return "teamDefaultUser.html";
		    }

		    if (team != null && team.getPresident() != null) {     //se si è autenticati
		        if (user.getPresident() == null) {                 //se l'user(che è presidente) non è associato al presidente allora, 
		        	                                               //viene settato il presidente della squadra(se non è nullo)
		            President newPresident = team.getPresident();  
		            newPresident.setUser(user);
		            this.presidentService.save(newPresident);

		            user.setPresident(newPresident);
		            this.userService.saveUser(user);
		            this.teamService.save(team);

		            return "team.html";
		        } else if (user.getPresident() != null && user.getPresident().getFiscalCode().equals(team.getPresident().getFiscalCode())) {
		            return "team.html";
		        }
		    }

		    return "teamDefaultUser.html";
		}
		}
