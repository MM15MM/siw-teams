package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Player;

public interface PlayerRepository extends CrudRepository<Player, Long> {

	public boolean existsByNameAndSurname(String name, String surname);

	@Query("SELECT p FROM Player p WHERE p.team.id <> :teamId OR p.team IS NULL")
	public Iterable<Player> findPlayersNotinTeam(@Param("teamId")Long id);
}
