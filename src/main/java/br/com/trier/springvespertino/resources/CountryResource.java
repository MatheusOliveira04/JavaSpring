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

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.services.impl.CountryServiceImpl;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/country")
public class CountryResource {

	@Autowired
	private CountryServiceImpl service;
	
	@GetMapping("/{id}")
	public ResponseEntity<Country> findById(@PathVariable Integer id){
		return ResponseEntity.ok(service.findById(id));
	}
	
	@GetMapping
	public ResponseEntity<List<Country>> findAll(){
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/nameContain/{contains}")
	public ResponseEntity<List<Country>> findByNameContainingIgnoreCase(@PathVariable String contains) {
		return ResponseEntity.ok(service.findByNameContainingIgnoreCase(contains));
	}
	
	@PostMapping
	public ResponseEntity<Country> insert(@RequestBody Country country){
		return ResponseEntity.ok(service.insert(country));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Country> update(@PathVariable Integer id, @RequestBody Country country){
		country.setId(id);
		return ResponseEntity.ok(service.update(country));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.ok().build();
	}
}
