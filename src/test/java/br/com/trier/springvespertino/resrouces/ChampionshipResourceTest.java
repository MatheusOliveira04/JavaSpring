package br.com.trier.springvespertino.resrouces;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.trier.springvespertino.SpringVespertinoApplication;
import br.com.trier.springvespertino.models.Championship;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/championship.sql")
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/resources/sqls/limpa_tabelas.sql")
@SpringBootTest(classes = SpringVespertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChampionshipResourceTest {

	@Autowired
	protected TestRestTemplate rest;
	
	private ResponseEntity<Championship> getChampionship(String url){
		return rest.getForEntity(url, Championship.class);
	}
	
	@Test
	@DisplayName("Busca por id")
	void findByIdTest() {
		ResponseEntity<Championship> re = rest.getForEntity("/championship/1",Championship.class);
		assertEquals(re.getStatusCode(), HttpStatus.OK);
		Championship cTest = re.getBody();
		assertEquals(1, cTest.getId());	
	}
	
	@Test
	@DisplayName("busca id não existe")
	void findByNotFound() {
		ResponseEntity<Championship> re = rest.getForEntity("/championship/10", Championship.class);
		assertEquals(re.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Listar todos")
	void listAllTest() {
		List<Championship> list = new ArrayList<>();
		ResponseEntity<List<Championship>> c = rest.exchange("/championship", HttpMethod.GET,
				null, new ParameterizedTypeReference<List<Championship>>() {});
		assertEquals(c.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@DisplayName("Teste inserir Championship")
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/resources/sqls/limpa_tabelas.sql")
	void insertTest() {
		var c = new Championship(1, "insert", 2000);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Championship> httpEntity = new HttpEntity<>(c, headers);
		ResponseEntity<Championship> re = rest.exchange("/championship", HttpMethod.POST, 
				httpEntity, Championship.class);
		assertEquals(re.getStatusCode(), HttpStatus.OK);
		c = null;
		c = re.getBody();
		assertEquals("insert", c.getDescription());
	}
	
	@Test
	@DisplayName("Teste atualiza")
	void updateTest() {
		var c = new Championship(null, "update", 2015);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Championship> httpEntity = new HttpEntity<>(c, header);
		ResponseEntity<Championship> re = rest.exchange("/championship/1", HttpMethod.PUT,
				httpEntity, Championship.class);
		assertEquals(re.getStatusCode(), HttpStatus.OK);
		assertEquals("update", c.getDescription());
	}
	
	@Test
	@DisplayName("Teste deletar")
	void deleteTest() {
		ResponseEntity<Void> re = rest.exchange("/championship/1", HttpMethod.DELETE,
				null, Void.class);
		assertEquals(re.getStatusCode(), HttpStatus.OK);
		
	}
}