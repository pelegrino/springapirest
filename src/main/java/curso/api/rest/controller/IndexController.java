package curso.api.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> init() {
		
		Usuario usuario = new Usuario();
		usuario.setId(50L);
		usuario.setLogin("pelegrino@gmail.com");
		usuario.setNome("Pelegrino");
		usuario.setSenha("admin");
		
		Usuario usuario2 = new Usuario();
		usuario2.setId(60L);
		usuario2.setLogin("matheuspelegrino@gmail.com");
		usuario2.setNome("Matheus Pelegrino");
		usuario2.setSenha("1234");
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(usuario);
		usuarios.add(usuario2);
		
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
	}

}
