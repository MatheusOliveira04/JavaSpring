package br.com.trier.springvespertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springvespertino.models.Pilot;

@Repository
public interface PilotRepository extends JpaRepository<Pilot, Integer>{
	
	List<Pilot> findByNameContainingIgnoreCaseOrderById(String name);
}