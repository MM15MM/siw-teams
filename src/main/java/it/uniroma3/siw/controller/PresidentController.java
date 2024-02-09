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
import it.uniroma3.siw.controller.validator.PresidentValidator;
import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PlayerService;
import it.uniroma3.siw.service.PresidentService;
import it.uniroma3.siw.service.TeamService;

@Controller
public class PresidentController {
	
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private PresidentService presidentService;

	@Autowired
	private PlayerValidator playerValidator;

	@Autowired
	private PresidentValidator presidentValidator;


	/*---------------------------------------------*/
	/*---------------------------------------------*/
	/*----------------PRESIDENTE-------------------*/
	/*---------------------------------------------*/
	/*---------------------------------------------*/
	
		
	
	/*AGGIUNTA E RIMOZIONE PLAYER DAL TEAM DA PARTE DEL PRESIDENTE*/
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
    model.addAttribute("player", new Player());
    
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
	
	@RequestMapping(value="/addNewPlayer/{id}", method = RequestMethod.POST)
	public String presidentAddPlayerToTeam(
	        @Valid @ModelAttribute("player") Player player,
	        BindingResult bindingResult,
	       @PathVariable("id") Long id,
           HttpServletRequest request, 
	        Model model) {
	  String referer = request.getHeader("Referer");

	  

	    this.playerValidator.validate(player, bindingResult);
	    Team team = this.teamService.findById(id);
	    String sport = team.getSport();
	    if (!bindingResult.hasErrors()) {
	        player.setTeam(team);
	        player.setSport(sport);
	        team.getPlayers().add(player);

	        this.playerService.save(player); 
	        this.teamService.save(team);
	        model.addAttribute("player", player);

	       

	        return "redirect:" + referer;
	    } else {
	        return "redirect:" + referer; 
	    }
	}
	

  
  @GetMapping(value = "/addNewPlayerToTeam/{id}")
	public String presidentAddPlayer(@PathVariable("id") Long id, Model model){
      
	  Player p = new  Player();
	  model.addAttribute("team", this.teamService.findById(id));
		model.addAttribute("player",p);
		return "formNewPlayerPresident.html";
	}
  
  
  
  
  
  /*AGGIUNTA PLAYER ESISTENTE NEL TEAM SE NON E' TESSERATO IN UN'ALTRA SQUADRA*/
  
  
	@RequestMapping(value="/addPlayerToTeam/{teamId}", method = RequestMethod.POST)
	public String addPlayerToTeam( @RequestParam("playerId") Long playerId, 
			@PathVariable("teamId") Long teamId, 
			@RequestParam("membershipEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam("membershipStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			HttpServletRequest request,
			Model model,RedirectAttributes redirectAttributes) {
		
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
			  String errorMessage = "Il giocatore non può essere aggiunto al team, potrai aggiungerlo quando terminerà il suo attuale tesseramento(" + player.getMembershipEndDate() + ")";
			  redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
			 System.out.println("Messaggio di errore impostato: " + errorMessage);
		 }
		    
		return "redirect:" + referer;
	}
	
	/*RIMOZIONE GIOCATORE DAL TEAM*/
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
            player.setMembershipEndDate(null);
            player.setMembershipStartDate(null);
            this.teamService.save(team);
            this.playerService.save(player);
        }
		
		return "redirect:" + referer;
	}
	
	/*PRESIDENTE VISUALIZZA PAGINA DEL SUO TEAM*/
	@GetMapping(value="/manageTeam")
	public String manageTeamPresident(Principal principal, Model model) {
		String username = principal.getName();
	    User user = this.credentialsService.getCredentials(username).getUser();
	    String fc = user.getFiscalCode();

	    // Cerca il team associato al presidente con il codice fiscale corrente
	    Team t = findPresident(fc);

	    if (t == null || t.getPresident() == null) {
	        return "errorPresident"; // Nessun presidente associato al team
	    } else {
	        Long id = t.getId();
	        model.addAttribute("team", t);
	        return "redirect:/team/" + id; // Ritorna la pagina del team
	    }
	}
	
	
	private Team findPresident(String fiscalCode) {
	  List<Team> teams = this.teamService.findAll(); 
	    for (Team team : teams) {
	        if (team.getPresident() != null && team.getPresident().getFiscalCode().equals(fiscalCode)) {
	            return team;
	        }
	    }
	    return null; // Nessun team trovato
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
			
		}
		
		return "admin/indexAdmin";
	}
	@GetMapping(value="/admin/newPresident")
	public String adminNewPresidentGetMapping(Model model) {
		
		model.addAttribute("president", new President());
		return "admin/formNewPresident";
	
	}
	@RequestMapping(value="/removePresidentFromTeam/{teamId}/{presidentId}",method = RequestMethod.POST)
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
	@RequestMapping(value="/addPresidentToTeam/{teamId}", method = RequestMethod.POST)
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
	
/*ELIMINA PRESIDENT DAL SISTEMA*/
	
	@RequestMapping(value = "/admin/deletePresident/{id}", method=RequestMethod.GET)
	public String deletePresident(@PathVariable("id") Long id) {
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
