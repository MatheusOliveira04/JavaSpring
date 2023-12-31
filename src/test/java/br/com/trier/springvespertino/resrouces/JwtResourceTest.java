package br.com.trier.springvespertino.resrouces;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import br.com.trier.springvespertino.SpringVespertinoApplication;
import br.com.trier.springvespertino.config.jwt.LoginDTO;
import br.com.trier.springvespertino.models.dto.UserDTO;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = SpringVespertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JwtResourceTest {

	@Autowired
	TestRestTemplate rest;
	
	@Test
	@DisplayName("Obter Token")
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void getToken() {
		LoginDTO loginDTO = new LoginDTO("User1@gmail.com", "111");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);
		ResponseEntity<String> responseEntity = rest.exchange(
				"/auth/token", 
				HttpMethod.POST,  
				requestEntity,    
				String.class   
				);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		System.out.println("****************"+responseEntity.getBody());
		HttpHeaders headers2 = new HttpHeaders();
		headers2.setBearerAuth(responseEntity.getBody());
		ResponseEntity<List<UserDTO>> response =  rest.exchange(
				"/users", HttpMethod.GET, new HttpEntity<>(headers2),
				new ParameterizedTypeReference<List<UserDTO>>() {} , headers2);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
}
