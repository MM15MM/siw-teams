package it.uniroma3.siw.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.controller.validator.PresidentValidator;
import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PresidentService;
import it.uniroma3.siw.service.TeamService;

@Controller
public class PresidentController {
	
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private PresidentService presidentService;

	@Autowired
	private PresidentValidator presidentValidator;

	/*---------------------------------------------*/
	/*---------------------------------------------*/
	/*----------------PRESIDENTE-------------------*/
	/*---------------------------------------------*/
	/*---------------------------------------------*/

	/*PRESIDENTE VISUALIZZA PAGINA DEL SUO TEAM*/
	@GetMapping(value="/manageTeam")
	public String manageTeamPresident(Principal principal, Model model) {
		String username = principal.getName();
	    User user = this.credentialsService.getCredentials(username).getUser();
	    String c = user.getCode();
        President p = this.presidentService.findByCode(c);
        
	    // Cerca il team associato al presidente con il codice fiscale corrente
	    Team t = p.getTeam();

	    if (t == null || t.getPresident() == null) {
	        return "errorPresident"; // Nessun presidente associato al team
	    } else {
	        Long id = t.getId();
	        model.addAttribute("team", t);
	        return "redirect:/team/" + id; // Ritorna la pagina del team
	    }
	}
		
	
	/*---------------------------------------------*/
	/*---------------------------------------------*/
	/*----------------ADMIN------------------------*/
	/*---------------------------------------------*/
	/*---------------------------------------------*/
	
	/*ADMIN AGGIUNGE PRESIDENTE AL SISTEMA*/
	
	@PostMapping(value="/admin/newPresident")
	public String adminNewPresident( @Valid @ModelAttribute("president") President president, 
            BindingResult bindingResult,
            HttpServletRequest request, Model model) {
		
		this.presidentValidator.validate(president, bindingResult);
		if(!bindingResult.hasErrors()) {
			
			this.presidentService.save(president);
			model.addAttribute("president", president);
			return "admin/indexAdmin";
		}
		
		return "admin/formNewPresident";
	}
	@GetMapping(value="/admin/newPresident")
	public String adminNewPresidentGetMapping(Model model) {
		
		model.addAttribute("president", new President());
		return "admin/formNewPresident";
	
	}
	@PostMapping(value="/removePresidentFromTeam/{teamId}/{presidentId}")
	public String removePresidentFromTeam(@PathVariable("presidentId") Long presidentId, 
			@PathVariable("teamId") Long teamId,HttpServletRequest request, Model model) {
		
		String referer = request.getHeader("Referer");//per aggiornare la pagina
		Team team = this.teamService.findById(teamId);
		President president = this.presidentService.findById(presidentId);
		
		if ((team.getPresident().equals(president)) &&
				(president.getTeam() != null)) {
            team.setPresident(null);
            president.setTeam(null);
            this.teamService.save(team);
            this.presidentService.save(president);
        }
		
		return "redirect:" + referer;
	}
	
	/*AGGIUNGE PRESIDENTE AL TEAM*/
	@PostMapping(value="/addPresidentToTeam/{teamId}")
	public String addPresidentToTeam( @RequestParam("presidentId") Long presidentId, 
			@PathVariable("teamId") Long teamId, HttpServletRequest request,
			Model model,RedirectAttributes redirectAttributes) {
		
		String referer = request.getHeader("Referer");// per aggiornare la pagina
	    	
		Team team = this.teamService.findById(teamId);
		President president = this.presidentService.findById(presidentId);
		
		 
		  if ((team.getPresident()==null)
					&&(president.getTeam() == null )) {
			 
			    team.setPresident(president);
	            president.setTeam(team);
	            this.teamService.save(team);
	            this.presidentService.save(president);
			
	        }
		
		    
		return "redirect:" + referer;
	}
	
	/*ADMIN ELIMINA PRESIDENTE*/
	@GetMapping(value="/admin/deletePresident/{id}")
	public String adminNewPresidentGetMapping(@PathVariable("id") Long id,Model model) {
		President p = this.presidentService.findById(id);
            if(p.getUser()!=null) {
			this.credentialsService.deleteByUserId(p.getUser().getId());
		}
            this.presidentService.deleteById(id);
		return "redirect:/admin/presidents";
	
	}

	
/*ADMIN VISUALIZZA DETTAGLI PRESIDENTE*/
	
	@GetMapping (value="/admin/president/{id}")
	public String showPresidentDetailsAdmin(@PathVariable("id") Long id ,Model model){
		model.addAttribute("president", this.presidentService.findById(id));
		return "admin/president";
	}
	
   /*ADMIN VISUALIZZA LISTA PRESIDENTI*/
	
	@GetMapping(value = "/admin/presidents")
	public String getPresidentsAdmin(Model model, @RequestParam(name = "sport", required = false) String sport) {
	    List<President> presidents;

	    if (sport != null && !sport.isEmpty()) {
	        //  recupera solo presidenti di quello specifico sport
	    	presidents = this.presidentService.findPresidentsBySport(sport);
	    } else {
	        // altrimenti recupera tutti i presidenti
	    	presidents = this.presidentService.findAll();
	    }

	    model.addAttribute("presidents", presidents);
	    model.addAttribute("selectedSport", sport);

	    // Aggiungi tutti gli sport disponibili
	    List<String> sports = this.teamService.getSports();
	    model.addAttribute("sports", sports);

	    return "admin/presidents";
	}
		
	
	
	
	

}
