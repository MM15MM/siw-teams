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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.controller.validator.PlayerValidator;
import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PlayerService;
import it.uniroma3.siw.service.TeamService;

@Controller
public class PlayerController {
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private PlayerValidator playerValidator;

	@Autowired
	private TeamService teamService;

	@Autowired
	private CredentialsService credentialsService;
	
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
			
	if ((p.getSport().equals(team.getSport()))) {
				
				players.add(p);
			}
		}
		return players;
	}

	
	
	
	
	/*PRESIDENT AGGIUNGE GIOCATORE NON PRESENTE NEL SISTEMA E LO AGGIUNGE ALLA SQUADRA*/
	
	@PostMapping(value="/addNewPlayer/{id}")
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
	
  
  
  
  
  /*AGGIUNTA PLAYER ESISTENTE NEL TEAM SE NON E' TESSERATO IN UN'ALTRA SQUADRA*/
  
 	@PostMapping(value="/addPlayerToTeam/{teamId}")
	public String addPlayerToTeam( @RequestParam("playerId") Long playerId, 
			@PathVariable("teamId") Long teamId, 
			@RequestParam("membershipEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam("membershipStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			HttpServletRequest request,
			Model model,RedirectAttributes redirectAttributes) {
		
		String referer = request.getHeader("Referer");// per aggiornare la pagina
	    	
		Team team = this.teamService.findById(teamId);
		Player player = this.playerService.findById(playerId);
		
		 
		  if ((player.getTeam() == null) || 
                  (player.getMembershipEndDate().isBefore(LocalDate.now()))) {
			 
			    team.getPlayers().add(player);
	            player.setTeam(team);
	            player.setMembershipStartDate(startDate);
	            player.setMembershipEndDate(endDate);
	            this.teamService.save(team);
	            this.playerService.save(player);
			
	        }
		  else {
		        // Aggiungi un attributo al model con il messaggio di errore
			  String errorMessage = "Il giocatore non può essere aggiunto al team, potrai aggiungerlo quando terminerà il suo attuale tesseramento(" + player.getMembershipEndDate() + ")";
			  redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
		 }
		    
		return "redirect:" + referer;
	}
	
	/*RIMOZIONE GIOCATORE DAL TEAM*/
	@PostMapping(value="/removePlayerFromTeam/{teamId}/{playerId}")
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
           // player.setMembershipStartDate(null);
            this.teamService.save(team);
            this.playerService.save(player);
        }
		
		return "redirect:" + referer;
	}
	
	
	/*---------------------------------------------*/
	/*---------------------------------------------*/
	/*----------------ADMIN------------------------*/
	/*---------------------------------------------*/
	/*---------------------------------------------*/
	
	/*ELIMINA PLAYER DAL SISTEMA*/
	
	@GetMapping(value = "/admin/deletePlayer/{id}")
	public String deletePlayer(@PathVariable("id") Long id, Model model) {
        this.playerService.deleteById(id);
		return "redirect:/admin/players";
	}

	/* AGGIUNGI NUOVO PLAYER NEL SISTEMA SENZA LA SQUADRA)*/
	
/*	@PostMapping(value ="/admin/newPlayer")
	public String newPlayer(@Valid @ModelAttribute("player") Player player, 
			                BindingResult bindingResult,@RequestParam(name = "sport", required = true) String sport, Model model) {
		List<String> sports = this.teamService.getSports();
		model.addAttribute("sports", sports);
		this.playerValidator.validate(player, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.playerService.save(player); 
			model.addAttribute("player", player);
			 model.addAttribute("sport", sport); // Passa lo sport alla vista

			return "admin/player.html";
		   } 
		else {
			return "admin/formNewPlayer.html"; 
		}
	}
	
	@GetMapping(value = "/admin/formNewPlayer")
	public String addPlayer(Model model){
		model.addAttribute("player",new Player());
		List<String> sports = this.teamService.getSports();
	    model.addAttribute("sports", sports); //   sport 
	   
		return "admin/formNewPlayer.html";
	}*/
	
	
	/*ADMIN VISUALIZZA DETTAGLI GIOCATORE*/
	
	@GetMapping (value="/admin/player/{id}")
	public String showPlayerDetailsAdmin(@PathVariable("id") Long id ,Model model){
		model.addAttribute("player", this.playerService.findById(id));
		return "admin/player";
	}
	
   /*ADMIN VISUALIZZA LISTA GIOCATORI*/
	
	@GetMapping(value = "/admin/players")
	public String getPlayersAdmin(Model model, @RequestParam(name = "sport", required = false) String sport) {
	    List<Player> players;

	    if (sport != null && !sport.isEmpty()) {
	        //  recupera solo i giocatori di quello specifico sport
	        players = this.playerService.findPlayersBySport(sport);
	    } else {
	        // altrimenti recupera tutti i giocatori
	        players = this.playerService.findAll();
	    }

	    model.addAttribute("players", players);
	    model.addAttribute("selectedSport", sport);

	    // Aggiungi tutti gli sport disponibili
	    List<String> sports = this.teamService.getSports();
	    model.addAttribute("sports", sports);

	    return "admin/players";
	}
	
	
	
	/*---------------------------------------------*/
	/*---------------------------------------------*/
	/*----------------GENERALE----------------------*/
	/*---------------------------------------------*/
	/*---------------------------------------------*/
	
	
	/* CHIUNQUE VISUALIZZA I DETTAGLI DEL GIOCATORE E LA LISTA DEI GIOCATORI*/
	
	@GetMapping(value = "/player/{id}")
	public String getPlayer(@PathVariable("id") Long id, Model model) {
		model.addAttribute("player", this.playerService.findById(id));
		return "player.html";
	}

	@GetMapping(value = "/players")
	public String getPlayers(Model model, @RequestParam(name = "sport", required = false) String sport) {
        List<Player> players;

        if (sport != null && !sport.isEmpty()) {
            //  recupera solo i giocatori di quello specifico sport
           players = this.playerService.findPlayersBySport(sport);
        } else {
            // altrimenti recupera tutti i giocatori
            players = this.playerService.findAll();
        }

        model.addAttribute("players", players);
        model.addAttribute("selectedSport", sport);

        // Aggiungi tutti gli sport disponibili 
        List<String> sports = this.teamService.getSports();
        model.addAttribute("sports", sports);

        return "players";
		
	}

	
	
	
}
