package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.PlayerValidator;
import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.service.PlayerService;

@Controller
public class PlayerController {
	
	@Autowired
	PlayerService playerService;
	
	@Autowired
	PlayerValidator playerValidator;
	
	
	/*ELIMINA PLAYER DAL SISTEMA*/
	
	@PostMapping(value = "/admin/deletePlayer/{id}")
	public String deletePlayer(@PathVariable("id") Long id) {
        this.playerService.deleteById(id);
		return "redirect:/admin/players";
	}

	/* AGGIUNGI NUOVO PLAYER NEL SISTEMA(SOLO ADMIN)*/
	
	@PostMapping(value ="/admin/newPlayer")
	public String newPlayer(@Valid @ModelAttribute("player") Player player, 
			                BindingResult bindingResult, Model model) {
		
		this.playerValidator.validate(player, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.playerService.save(player); 
			model.addAttribute("player", player);
			return "admin/player.html";
		   } 
		else {
			return "admin/formNewPlayer.html"; 
		}
	}
	
	@GetMapping(value = "/admin/formNewPlayer")
	public String addPlayer(Model model){
		model.addAttribute("player",new Player());
		return "admin/formNewPlayer.html";
	}
	
	/* CHIUNQUE VISUALIZZA I DETTAGLI DELL'ARTISTA E LA LISTA DEGLI ARTISTI*/
	
	@GetMapping(value = "/player/{id}")
	public String getPlayer(@PathVariable("id") Long id, Model model) {
		model.addAttribute("player", this.playerService.findById(id));
		return "player.html";
	}

	@GetMapping(value = "/players")
	public String getPlayers(Model model) {
		model.addAttribute("players", this.playerService.findAll());
		return "players.html";
	}
	
	/*ADMIN VISUALIZZA DETTAGLI ARTISTA*/
	
	@GetMapping (value="/admin/player/{id}")
	public String showPlayerDetailsAdmin(@PathVariable("id") Long id ,Model model){
		model.addAttribute("player", this.playerService.findById(id));
		return "admin/player";
	}
	
   /*ADMIN VISUALIZZA LISTA ARTISTI*/
	
	@GetMapping(value="/admin/players")
	public String indexPlayer(Model model) {
		model.addAttribute("players", this.playerService.findAll());
		return "admin/players.html";
	}
	
	
	
}
