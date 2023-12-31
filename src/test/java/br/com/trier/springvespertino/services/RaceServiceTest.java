package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTests;
import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.Speedway;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.utils.DateUtil;
import jakarta.transaction.Transactional;

@Transactional
public class RaceServiceTest extends BaseTests{

	@Autowired
	RaceService service;
	
	@Autowired
	SpeedwayService speedwayService;
	
	@Autowired
	ChampionshipService championshipService;
	
	@Test
	@DisplayName("Teste buscar por id")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void findByIdTest() {
		Race race = service.findById(1);
		assertNotNull(race);
		assertEquals(1, race.getId());
		assertEquals("World Cup", race.getChampionship().getDescription());
	}
	
	@Test
	@DisplayName("Teste buscar por id sem cadastro")
	void findByIdNotFoundTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> service.findById(10));
		assertEquals("Id: 10 da corrida não encontrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste listar todos")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void ListAllTest() {
		List<Race> list = service.findAll();
		assertEquals(2, list.size());
		assertEquals(1, list.get(0).getId());
		assertEquals(2, list.get(1).getId());
	}
	
	@Test
	@DisplayName("Teste listar todos sem cadastro")
	void ListAllEmptyTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> service.findAll());
		assertEquals("Nenhum corrida encontrado", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste inserir")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	void insertTest() {
		Race race = new Race(null, ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0, 0), 
				ZoneId.of("America/Sao_Paulo")), speedwayService.findById(1), championshipService.findById(1));
		race = service.insert(race);
		assertNotNull(race);
		assertEquals(1, race.getId());
		assertEquals(1, race.getSpeedway().getId());
		assertEquals(1, race.getChampionship().getId());
	}
	@Test
	@DisplayName("Teste inserir com ano do campeonato diferente da data")
	void insertCountryNullTest() {
		Speedway speed = new Speedway(1, "insert", 100, new Country(1, "insert"));
		Championship champ = new Championship(1, "insert", 2000);
		Race race = new Race(1, ZonedDateTime.of(LocalDateTime.of(1900, 1, 1, 0, 0, 0), 
				ZoneId.of("America/Sao_Paulo")), speed, champ);
		var exception = assertThrows(IntegrityViolation.class,() -> service.insert(race));
		assertEquals("Ano data de corrida é diferente de ano do campeonato", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste inserir com data nulo")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	void insertDateNullTest() {
		Speedway speedway = speedwayService.findById(1);
		Championship championship = championshipService.findById(1);
		Race race = new Race(1, null, speedway, championship);
		var exception = assertThrows(IntegrityViolation.class, () -> service.insert(race));
		assertEquals("Data está vazia", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste atualizar")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void updateTest() {
		Race race = new Race(1, ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0, 0), 
				ZoneId.of("America/Sao_Paulo")), 
				speedwayService.findById(2), 
				championshipService.findById(2));
		race = service.update(race);
		assertNotNull(race);
		assertEquals(1, race.getId());
		assertEquals(2023, race.getDate().getYear());
		assertEquals(2, race.getSpeedway().getId());
		assertEquals(2, race.getChampionship().getId());
	}
	
	@Test
	@DisplayName("Teste atualizar com ano do campeonato diferente da data")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void updateDateInvalid(){
		Speedway speed = new Speedway(1, "update", 100, new Country(1, "update"));
		Championship champ = new Championship(1, "update", 2000);
		var exception = assertThrows(IntegrityViolation.class,() -> service.update(new Race(1, ZonedDateTime.of(LocalDateTime.of(1900, 1, 1, 0, 0, 0), 
				ZoneId.of("America/Sao_Paulo")), speed, champ)));
		assertEquals("Ano data de corrida é diferente de ano do campeonato", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste atualizar com data nulo")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void updateDateNullTest() {
		Speedway speedway = speedwayService.findById(1);
		Championship championship = championshipService.findById(1);
		Race race = new Race(1, null, speedway, championship);
		var exception = assertThrows(IntegrityViolation.class, () -> service.update(race));
		assertEquals("Data está vazia", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste atualiza com id inexistente")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void updateIdNotFoundTest() {
		Race race = new Race(10, ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0, 0), 
				ZoneId.of("America/Sao_Paulo")), 
				speedwayService.findById(2), 
				championshipService.findById(2));
		var exception = assertThrows(ObjectNotFound.class, () -> service.update(race));
		assertEquals("Id: 10 da corrida não encontrado", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste deletar")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void deleteTest() {
		List<Race> list = service.findAll();
		assertEquals(2, list.size());
		service.delete(1);
		list = service.findAll();
		assertEquals(1, list.size());
	}
	
	@Test
	@DisplayName("Teste delete id não encotrado")
	void deleteNotFoundId(){
		var exception = assertThrows(ObjectNotFound.class, () -> service.delete(10));
		assertEquals("Id: 10 da corrida não encontrado", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste buscar por id pista")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void findBySpeedwayTest() {
		List<Race> list = service.findBySpeedwayOrderById(speedwayService.findById(1));
		assertEquals(2, list.size());
	}
	
	@Test
	@DisplayName("Teste buscar por id pista, nenhum encontrado")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void findBySpeedwayNotFound() {
		var exception = assertThrows(ObjectNotFound.class, 
				() -> service.findBySpeedwayOrderById(speedwayService.findById(2)));
		assertEquals("pista 2 não encontrado na corrida", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste buscar por id campeonato")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void findByChampionshipTest() {
		List<Race> list = service.findByChampionshipOrderById(championshipService.findById(1));
		assertEquals(2, list.size());
	}
	
	@Test
	@DisplayName("Teste buscar por id campeonato nenhum encontrado")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void findByChampionshipNotFound() {
		var exception = assertThrows(ObjectNotFound.class, 
				() -> service.findByChampionshipOrderById(championshipService.findById(2)));
		assertEquals("campeonato 2 não encontrado na corrida", exception.getMessage());
	}
	
	@Test
	@DisplayName("Buscar todas corridas ocorridas depois da data")
	@Sql({"classpath:/resources/sqls/country.sql"})
	@Sql({"classpath:/resources/sqls/speedway.sql"})
	@Sql({"classpath:/resources/sqls/championship.sql"})
	@Sql({"classpath:/resources/sqls/race.sql"})
	void findByDateAfterTest() {
		List<Race> list = service.findByDateAfter(ZonedDateTime.of(
				LocalDateTime.of(2000, 1, 1, 0, 0, 0), 
				ZoneId.systemDefault()));
		assertEquals(2, list.size());
	}
	
	@Test
	@DisplayName("Buscar todas corridas ocorridas depois da data com nenhuma corrida encontrada")
	void findByDAteAfterNotFound() {
		var exception = assertThrows(ObjectNotFound.class,
				() -> service.findByDateAfter(ZonedDateTime.of(
				LocalDateTime.of(2000, 1, 1, 0, 0, 0), 
				ZoneId.systemDefault())));
		assertEquals(
				"Nenhuma corrida encontrada com esta data: 2000-01-01T00:00-02:00[America/Sao_Paulo]",
				exception.getMessage());
	}
}
