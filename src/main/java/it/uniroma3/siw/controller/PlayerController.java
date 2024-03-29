package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.service.PlayerService;
import it.uniroma3.siw.service.TeamService;

@Controller
public class PlayerController {
	
	@Autowired
	private PlayerService playerService;
	
	/*@Autowired
	private PlayerValidator playerValidator;*/

	@Autowired
	private TeamService teamService;
	
	
	
	/*---------------------------------------------*/
	/*---------------------------------------------*/
	/*----------------ADMIN------------------------*/
	/*---------------------------------------------*/
	/*---------------------------------------------*/
	
	/*ELIMINA PLAYER DAL SISTEMA*/
	
	@RequestMapping(value = "/admin/deletePlayer/{id}", method=RequestMethod.GET)
	public String deletePlayer(@PathVariable("id") Long id) {
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
