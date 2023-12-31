package br.com.trier.springvespertino.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Speedway;
import br.com.trier.springvespertino.repositories.SpeedwayRepository;
import br.com.trier.springvespertino.services.SpeedwayService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class SpeedwaySerivceImpl implements SpeedwayService{
	
	@Autowired
	private SpeedwayRepository repository;
	
	private void validateSpeedway(Speedway speedway) {
		if(speedway.getSize() == null || speedway.getSize() <= 0) {
			throw new IntegrityViolation("Tamanho da pista inválido");
		}
	}
	
	private void validateCountryIsNull(Speedway speedway) {
		if(speedway.getCountry() == null) {
			throw new IntegrityViolation("País da pista está nulo");
		}
	}
	
	@Override
	public Speedway findById(Integer id) {
		return repository.findById(id)
				.orElseThrow( () -> new ObjectNotFound("pista %s não existe".formatted(id)));
	}

	@Override
	public Speedway insert(Speedway speedway) {
		validateSpeedway(speedway);
		 validateCountryIsNull(speedway);
		return repository.save(speedway);
	}

	@Override
	public List<Speedway> listAll() {
		List<Speedway> lista = repository.findAll();
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada");
		}
		return lista;
	}

	@Override
	public Speedway update(Speedway speedway) {
		findById(speedway.getId());
		validateSpeedway(speedway);
		validateCountryIsNull(speedway);
		return repository.save(speedway);
	}

	@Override
	public void delete(Integer id) {
		Speedway speedway = findById(id);
		repository.delete(speedway);
	}

	@Override
	public List<Speedway> findByNameStartsWithIgnoreCase(String name) {
		List<Speedway> lista = repository.findByNameStartsWithIgnoreCase(name);
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada com esse nome");
		}
		return lista;
	}

	@Override
	public List<Speedway> findBySizeBetween(Integer sizeIn, Integer sizeFin) {
		List<Speedway> lista = repository.findBySizeBetween(sizeIn, sizeFin);
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada com essas medidas");
		}
		return lista;
	}

	@Override
	public List<Speedway> findByCountryOrderBySizeDesc(Country country) {
		List<Speedway> lista = repository.findByCountryOrderBySizeDesc(country);
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada no país: %s".formatted(country.getName()));
		}
		return lista;
	}

}
