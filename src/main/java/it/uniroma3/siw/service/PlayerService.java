package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.repository.PlayerRepository;

@Service
public class PlayerService {
	
	@Autowired
	private PlayerRepository playerRepository;

	@Transactional
	public Player findById(Long playerId) {
		// TODO Auto-generated method stub
		return this.playerRepository.findById(playerId).get();
	}

	@Transactional
	public void save(Player player) {
		// TODO Auto-generated method stub
		this.playerRepository.save(player);
	}

	@Transactional
	public List<Player> findPlayersNotInTeam(Long id) {
		// TODO Auto-generated method stub
		return (List<Player>) this.playerRepository.findPlayersNotinTeam(id);
	}

	@Transactional
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		this.playerRepository.deleteById(id);
	}

	@Transactional
	public List<Player> findAll() {
		// TODO Auto-generated method stub
		return (List<Player>)this.playerRepository.findAll();
	}


}
