package br.com.trier.springvespertino.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.services.impl.TeamServiceImpl;

@RestController
@RequestMapping("/team")
public class TeamResource {

	@Autowired
	TeamServiceImpl service;
	
	@PostMapping
	public ResponseEntity<Team> insert(@RequestBody Team team){
		Team newTeam = service.insert(team);
		return newTeam != null ? ResponseEntity.ok(newTeam) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Team> findById(@PathVariable Integer id){
		Team newTeam = service.findById(id);
		return newTeam != null ? ResponseEntity.ok(newTeam) : ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public ResponseEntity<List<Team>> findAll(){
		List<Team> list = service.findAll();
		return list.size() > 0 ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<Team> findByNameIgnoreCase(@PathVariable String name){
		Team team = service.findByNameIgnoreCase(name);
		return team != null ? ResponseEntity.ok(team) : ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Team> update(@PathVariable Integer id, @RequestBody Team team){
		team.setId(id);
		Team newTeam = service.update(team);
		return newTeam != null ? ResponseEntity.ok(newTeam) : ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.ok().build();
	}
}