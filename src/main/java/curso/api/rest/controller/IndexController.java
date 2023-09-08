package curso.api.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<String> init(
			@RequestParam(value = "nome", defaultValue = "O nome não informado,", required = true) String nome, 
			@RequestParam(value = "salario", defaultValue = "0", required = true) Long salario) {
		return new ResponseEntity<String>("Olá Usuário Rest Spring Boot, seu nome é: " + nome + " e seu salário é de R$ " + salario, HttpStatus.OK);
	}

}
