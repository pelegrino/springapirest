package curso.api.rest.dto;

import java.io.Serializable;

import curso.api.rest.model.Usuario;

public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String userLogin;
	private String userNome;
	
	public UsuarioDTO(Usuario usuario) {

		this.userLogin = usuario.getLogin();
		this.userNome = usuario.getNome();
	
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserNome() {
		return userNome;
	}

	public void setUserNome(String userNome) {
		this.userNome = userNome;
	}

}
