package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.repository.TeamRepository;

@Service
public class TeamService {
	
	@Autowired
    private TeamRepository teamRepository;

	@Transactional
	public List<Team> findAll() {
		// TODO Auto-generated method stub
		return (List<Team>)this.teamRepository.findAll();
	}

	@Transactional
	public Team findById(Long id) {
		// TODO Auto-generated method stub
		return this.teamRepository.findById(id).get();
	}

	@Transactional
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		this.teamRepository.deleteById(id);
		
	}

	@Transactional
	public void save(Team team) {
		// TODO Auto-generated method stub
		this.teamRepository.save(team);
	}

	@Transactional
	public List<Team> findByName(String name) {
		// TODO Auto-generated method stub
		return this.teamRepository.findByName(name);
	}
	
	
	
	
	
	
}
