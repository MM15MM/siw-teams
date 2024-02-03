package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Player;

public interface PlayerRepository extends CrudRepository<Player, Long> {

	public boolean existsByNameAndSurname(String name, String surname);

	/*@Query("SELECT p FROM Player p WHERE p.team.id <> :teamId OR p.team IS NULL")
	public Iterable<Player> findPlayersNotinTeam(@Param("teamId")Long id);*/

	public List<Player> findBySport(String sport);

}
