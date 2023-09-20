package curso.api.rest.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import curso.api.rest.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
	
	@Query("select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String Login);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update usuario set token =?1 where login = ?2")
	void atualizaTokenUser(String token, String login);
	
}
