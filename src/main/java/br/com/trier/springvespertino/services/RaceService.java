package br.com.trier.springvespertino.services;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.Speedway;

public interface RaceService {

	List<Race> findAll();
	
	Race findById(Integer id);
	
	Race insert(Race race);
	
	Race update(Race race);
	
	void delete(Integer id);
	
	List<Race> findByDateAfter(ZonedDateTime date);
	
	List<Race> findBySpeedwayOrderById(Speedway speedway);
	
	List<Race> findByChampionshipOrderById(Championship championship);
}
