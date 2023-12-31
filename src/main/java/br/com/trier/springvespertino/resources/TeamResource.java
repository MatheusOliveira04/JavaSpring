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
		return ResponseEntity.ok(service.insert(team));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Team> findById(@PathVariable Integer id){
		return ResponseEntity.ok(service.findById(id));
	}
	
	@GetMapping
	public ResponseEntity<List<Team>> findAll(){
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<Team> findByNameIgnoreCase(@PathVariable String name){
		return ResponseEntity.ok(service.findByNameIgnoreCase(name));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Team> update(@PathVariable Integer id, @RequestBody Team team){
		team.setId(id);
		return ResponseEntity.ok(service.update(team));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.ok().build();
	}
}
