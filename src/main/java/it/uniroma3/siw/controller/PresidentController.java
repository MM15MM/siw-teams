package it.uniroma3.siw.controller;

import java.security.Principal;
import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.PresidentValidator;
import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PlayerService;
import it.uniroma3.siw.service.PresidentService;
import it.uniroma3.siw.service.TeamService;
import it.uniroma3.siw.service.UserService;

@Controller
public class PresidentController {
	
	
	@Autowired
	 private PresidentValidator presidentValidator;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PresidentService presidentService;

	/*AGGIUNTA E RIMOZIONE PLAYER DAL TEAM*/
	@GetMapping(value="/updatePlayers/{id}")
	public String updatePlayers(@PathVariable("id") Long id, Model model, Principal principal) {
        
		Team team = this.teamService.findById(id);
		
		model.addAttribute("playersToAdd", this.playerService.findPlayersNotInTeam(id));
		model.addAttribute("team", this.teamService.findById(id));
		
		String username = principal.getName();
        User user = this.credentialsService.getCredentials(username).getUser();
    if(!user.getPresident().equals(team.getPresident())) {
    	return "errorPresident";
    }
    else {
		return "playerTeam.html";
    }
    
	}

	//@RequestParam("membershipStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	@RequestMapping(value="/addPlayerToTeam/{teamId}", method = RequestMethod.POST)
	public String addPlayerToTeam(@RequestParam("playerId") Long playerId, 
			@PathVariable("teamId") Long teamId,
			@RequestParam("membershipEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			HttpServletRequest request,
			Model model) {
		
		String referer = request.getHeader("Referer");//uso HttpServletREquest per aggiornare la pagina
	    	
		Team team = this.teamService.findById(teamId);
		Player player = this.playerService.findById(playerId);
		
		 if (!(team.getPlayers().contains(player)) && 
				 (player.getTeam() == null || 
				 player.getMembershipEndDate() == null  || 
				 player.getMembershipEndDate().isBefore(LocalDate.now()))) {
	            
			    team.getPlayers().add(player);
	            player.setTeam(team);
	            player.setMembershipStartDate(LocalDate.now());
	            player.setMembershipEndDate(endDate);
	            this.teamService.save(team);
	            this.playerService.save(player);
	        }
		
		return "redirect:" + referer;

	}
	
	@RequestMapping(value="/removePlayerFromTeam/{teamId}/{playerId}",method = RequestMethod.POST)
	public String removePlayerFromTeam(@PathVariable("playerId") Long playerId, 
			@PathVariable("teamId") Long teamId,HttpServletRequest request, Model model) {
		
		String referer = request.getHeader("Referer");//uso HttpServletREquest per aggiornare la pagina
		Team team = this.teamService.findById(teamId);
		Player player = this.playerService.findById(playerId);
		
		if ((team.getPlayers().contains(player)) &&
				(player.getTeam() != null)) {
            team.getPlayers().remove(player);
            player.setTeam(null);
            player.setMembershipEndDate(LocalDate.now());
            this.teamService.save(team);
            this.playerService.save(player);
        }
		
		return "redirect:" + referer;
	}
	
	/*SALVATAGGIO PRESIDENTE*/
	@GetMapping(value="/registerPresident/{id}")
	  public String registerPresident(@PathVariable("id") Long id,Principal principal, Model model,HttpServletRequest request) {
		
		 String referer = request.getHeader("Referer");//uso HttpServletREquest per aggiornare la pagina
		if (principal != null) {
			Team team= this.teamService.findById(id);
	        String username = principal.getName();
	        User user = this.credentialsService.getCredentials(username).getUser();

	        // Crea un nuovo oggetto President e imposta alcuni valori predefiniti se necessario
	        President newPresident = new President();
	        newPresident.setName(user.getName());
	        newPresident.setSurname(user.getSurname());
	        newPresident.setFiscalCode(user.getFiscalCode());
	        newPresident.setBirthDate(user.getBirthDate());
	        newPresident.setPlaceOfBirth(user.getPlaceOfBirth());
	        newPresident.setTeam(team);
	        newPresident.setUser(user);
	        
	        this.presidentService.save(newPresident);
            user.setPresident(newPresident);
	        
	        this.userService.saveUser(user);

	        // Aggiungi il nuovo presidente al modello
	        model.addAttribute("president", newPresident);

		}
		return "redirect:" + referer;
	}
	/*  public String registerUserPresident(@Valid @ModelAttribute("president") President president,
			  @Valid @ModelAttribute("user") User user,
              @RequestParam(value = "teamId", required = false) Long teamId,
              @ModelAttribute("credentials") Credentials credentials,
              BindingResult credentialsBindingResult,
              BindingResult userBindingResult,
              BindingResult presidentBindingResult, Model model ){
		
		this.presidentValidator.validate(president, presidentBindingResult);
		if(!presidentBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()
				&& !userBindingResult.hasErrors()) {
	   
	    
        credentials.setUser(user);
    
		Team team = this.teamService.findById(teamId);
        /*president = new President();
        president.setName(name);
        president.setSurname(surname);
        president.setFiscalCode(fiscalCode);
        president.setBirthDate(LocalDate.parse(birthDate));*/
      /*  president.setUser(user);
        user.setPresident(president);
        president.setTeam(team);
        team.setPresident(president);
        this.teamService.save(team);
        this.presidentService.save(president);
        this.userService.saveUser(user);
        this.credentialsService.saveCredentials(credentials);
        model.addAttribute("president", president);
        model.addAttribute("user", user);
        return "registrationSuccessful.html";
	}
	return "formRegister.html";
}*/
	
	/*SALVATAGGIO PRESIDENTE*/
	/*@PostMapping(value="/registerPresident/{id}")
	  public String registerUserPresident(@Valid @ModelAttribute("president") President president,
              @PathVariable("id") Long teamId,Principal principal,
              BindingResult presidentBindingResult, Model model ){
		if(principal!=null) {
			 String username = principal.getName();
		Team team = this.teamService.findById(teamId);
		User user= this.credentialsService.getCredentials(username).getUser();
		
		this.presidentValidator.validate(president, presidentBindingResult);
		if(!presidentBindingResult.hasErrors() && 
				team.getPresident()==null && 
				user.getPresident()==null) {

        president.setUser(user);
        user.setPresident(president);
        president.setTeam(team);
        System.out.println("Team ID: " + team.getId());
        this.presidentService.save(president);
        team.setPresident(president);
        
       // president = this.presidentService.merge(president);
        //this.presidentService.refresh(president);

        
       this.teamService.save(team);
        this.userService.saveUser(user);
        model.addAttribute("president", president);
        return "team.html";
	}
		}
	return "president.html";
}

@GetMapping(value = "/registerPresident/{id}")
public String formNewPresident(Model model, @PathVariable ("id") Long teamId) {
	model.addAttribute("president", new President());
	model.addAttribute("team", this.teamService.findById(teamId));
	return "president.html";
}*/
}
