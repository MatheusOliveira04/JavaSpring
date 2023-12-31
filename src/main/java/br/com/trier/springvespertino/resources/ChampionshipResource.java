package br.com.trier.springvespertino.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.services.ChampionshipService;

@RestController
@RequestMapping("/championship")
public class ChampionshipResource {
	
	@Autowired
	ChampionshipService service;
	
	@Secured({"ROLES_ADMIN"})
	@PostMapping
	public ResponseEntity<Championship> insert(@RequestBody Championship champ){
		Championship c = service.insert(champ);
		return ResponseEntity.ok(c); 
	}

	@Secured({"ROLES_USER"})
	@GetMapping("/{id}")
	public ResponseEntity<Championship> findById(@PathVariable Integer id){
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Secured({"ROLES_USER"})
	@GetMapping
	public ResponseEntity<List<Championship>> findAll(){
		return ResponseEntity.ok(service.findAll());
	}
	
	@Secured({"ROLES_ADMIN"})
	@PutMapping("/{id}")
	public ResponseEntity<Championship> update(@PathVariable Integer id, @RequestBody Championship c){
		c.setId(id);
		return ResponseEntity.ok(service.update(c));
	}
	
	@Secured({"ROLES_ADMIN"})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@Secured({"ROLES_USER"})
	@GetMapping("/year/between/{yearBefore}/{yearAfter}")
	public ResponseEntity<List<Championship>> findByBetweenYear(@PathVariable Integer yearBefore, @PathVariable Integer yearAfter){
		return ResponseEntity.ok(service.findByYearBetween(yearBefore, yearAfter));
	}
}
