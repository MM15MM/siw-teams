package it.uniroma3.siw.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.controller.validator.PlayerValidator;
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
	private TeamService teamService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PresidentService presidentService;

	@Autowired
	private PlayerValidator playerValidator;

	/*AGGIUNTA E RIMOZIONE PLAYER DAL TEAMDA PARTE DEL PRESIDENTE*/
	@GetMapping(value="/updatePlayers/{id}")
	public String updatePlayers(@PathVariable("id") Long id, Model model, Principal principal) {
        
		Team team = this.teamService.findById(id);
		
		
		String username = principal.getName();
        User user = this.credentialsService.getCredentials(username).getUser();
        
    if(!user.getPresident().equals(team.getPresident())) {
    	return "errorPresident";
    }
  
    List<Player> players = this.playersToAdd(team);
    
    model.addAttribute("playersToAdd", players);
    model.addAttribute("team", team);
    
		return "playerTeam.html";
    
    
	}
	
	public List<Player> playersToAdd(Team team){
		
		List<Player> players = new ArrayList<Player>();
		List<Player> playersPresenti=(List<Player>) this.playerService.findAll(); 
		
		for(Player p: playersPresenti) {
			
	if ((p.getSport().equals(team.getSport()) && (p.getTeam()==null || !(p.getTeam().equals(team))))) {
				
				players.add(p);
			}
		}
		return players;
	}

	
	
	
	
	/*PRESIDENT AGGIUNGE GIOCATORE NON PRESENTE NEL SISTEMA E LO AGGIUNGE ALLA SQUADRA*/
  
	@PostMapping(value="/addNewPlayer/{id}")
	public String presidentAddPlayerToTeam( @Valid @ModelAttribute("player") Player player, 
            BindingResult bindingResult, 
            @PathVariable("id") Long id,
            HttpServletRequest request, Model model) {
		Team team = this.teamService.findById(id); 
		String sport = team.getSport();
this.playerValidator.validate(player, bindingResult);
if(!bindingResult.hasErrors()) {
	            player.setSport(sport);
	            team.getPlayers().add(player);
	            player.setTeam(team);
				this.playerService.save(player); 
				this.teamService.save(team);
		model.addAttribute("player", player);
		System.out.println("Debug message before printing team ID");
		System.out.println("Team ID: " + team.getId());
		System.out.println("Debug message after printing team ID");
return "team";
} 
else {
return "formNewPlayerPresident.html"; 

}

	}
	/*@RequestMapping (value="/confermaInserimento/{id}/{playerId}", method= RequestMethod.POST)
	public String confermaInserimento(@PathVariable("id") Long id, @PathVariable ("playerId") Long idP,
			Model model) {
		Team team = this.teamService.findById(id);
		 String sport= team.getSport();
		 Player player= this.playerService.findById(idP);
		 team.getPlayers().add(player);
		 player.setSport(sport);
	     player.setTeam(team);
	     this.teamService.save(team);
	     this.playerService.save(player);
	     return "team.html";
	}*/
  
  @GetMapping(value = "/formNewPlayer/{id}")
	public String presidentAddPlayer(@PathVariable("id") Long id, Model model){
	  Team team = this.teamService.findById(id);
	  Player p = new  Player();
		model.addAttribute("player",p);
		  model.addAttribute("team", team);
		  
		return "formNewPlayerPresident.html";
	}
  
  
  
  
  
  
  
  
	//@RequestParam("membershipStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	@RequestMapping(value="/addPlayerToTeam/{teamId}", method = RequestMethod.POST)
	public String addPlayerToTeam( @RequestParam("playerId") Long playerId, 
			@PathVariable("teamId") Long teamId, 
			@RequestParam("membershipEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam("membershipStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			HttpServletRequest request,
			Model model) {
		
		String referer = request.getHeader("Referer");// per aggiornare la pagina
	    	
		Team team = this.teamService.findById(teamId);
		Player player = this.playerService.findById(playerId);
		
			 if (((!(team.getPlayers().contains(player)))
					&&(player.getTeam() == null || 
			 player.getMembershipEndDate() == null || 
			 player.getMembershipEndDate().isBefore(startDate)))) {
			 
			    team.getPlayers().add(player);
	            player.setTeam(team);
	            player.setMembershipStartDate(startDate);
	            player.setMembershipEndDate(endDate);
	            this.teamService.save(team);
	            this.playerService.save(player);
			
	        }
		 
		 else if( player.getMembershipEndDate().isAfter(startDate)) {
		        // Aggiungi un attributo al model con il messaggio di errore
		        model.addAttribute("errorMessage", "Il giocatore non può essere aggiunto al team, potrai aggiungerlo quando terminerà il suo attuale tesseramento("+ player.getMembershipEndDate()+")");
		    }
		    
		return "redirect:" + referer;
	}
	
	
	@RequestMapping(value="/removePlayerFromTeam/{teamId}/{playerId}",method = RequestMethod.POST)
	public String removePlayerFromTeam(@PathVariable("playerId") Long playerId, 
			@PathVariable("teamId") Long teamId,HttpServletRequest request, Model model) {
		
		String referer = request.getHeader("Referer");//per aggiornare la pagina
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
	  public String registerPresident(@PathVariable("id") Long id,Principal principal, 
			  Model model,HttpServletRequest request) {
		
		Team team= this.teamService.findById(id);
		 String referer = request.getHeader("Referer");//per aggiornare la pagina
		 if (principal != null && this.isPrincipalPresident(principal)) {
		        // L'utente è già presidente di un'altra squadra, reindirizza a una pagina di errore
		        return "errorPresident.html";
		    }
		if (principal != null) {
			
	        String username = principal.getName();
	        User user = this.credentialsService.getCredentials(username).getUser();

	        
	        if (user.getPresident() != null) {
	            // L'utente è già presidente di un'altra squadra, reindirizza a una pagina di errore
	            return "errorPresident.html";
	        }
	        // Crea un nuovo oggetto President e imposta alcuni valori predefiniti se necessario
	        President newPresident = new President();
	        newPresident.setName(user.getName());
	        newPresident.setSurname(user.getSurname());
	        newPresident.setFiscalCode(user.getFiscalCode());
	        newPresident.setBirthDate(user.getBirthDate());
	        newPresident.setPlaceOfBirth(user.getPlaceOfBirth());
	        newPresident.setTeam(team);
	        newPresident.setUser(user);
	        team.setPresident(newPresident);
	        
	       
	        this.presidentService.save(newPresident);
            user.setPresident(newPresident);
            this.teamService.save(team);
	        this.userService.saveUser(user);

	        // Aggiungi il nuovo presidente al modello
	        model.addAttribute("president", newPresident);

		}
		return "redirect:/team/"+ id;
	}
	
	// Metodo per verificare se il principal è già presidente
	private boolean isPrincipalPresident(Principal principal) {
	    String username = principal.getName();
	    User user = this.credentialsService.getCredentials(username).getUser();

	    // Verifica se l'utente è già presidente di una squadra
	    return user.getPresident() != null;
	}

}
