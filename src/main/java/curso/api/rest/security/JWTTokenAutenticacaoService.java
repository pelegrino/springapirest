package curso.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {

	private static final long EXPIRATION_TIME = 172800000;

	private static final String SECRET = "&*hb89UHB087Y$*+";
	
	//Prefixo padrão do token
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	//Gerando o token de autenticação e adicionando o cabeçalho e resposta http
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		
		//Montagem do token
		String JWT = Jwts.builder() //Chama o gerador do token
				.setSubject(username) //Adiciona o usuário
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //Informa o tempo para expiração
				.signWith(SignatureAlgorithm.HS512, SECRET).compact(); //Compactação e algortimos da geração do token
		
		//Junta o token com o prefixo
		String token = TOKEN_PREFIX + " " + JWT; //Bearer: 983974urnmk0g87y
		
		//Adiciona no cabeçalho http
		response.addHeader(HEADER_STRING, token); //Authorization: Bearer: 983974urnmk0g87y
		
		//Escreve token como resposta no corpo do http
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
		
	}
	
	//Retorna o usuário validado com token ou caso não seja validado retorna null
	public Authentication getAuthentication(HttpServletRequest request) {
		
		//Pega o token enviado no cabeçalho http
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {
			
			//Faz a validação do token do usuário na requisição
			String user = Jwts.parser()
							.setSigningKey(SECRET)
							.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
							.getBody()
							.getSubject();
			
			if (user != null) {
				Usuario usuario = ApplicationContextLoad.getApplicationContext()
									.getBean(UsuarioRepository.class)
									.findUserByLogin(user);
				
				if (usuario != null) {
					return new UsernamePasswordAuthenticationToken(
								usuario.getLogin(),
								usuario.getSenha(),
								usuario.getAuthorities());
				} 
			}
		}
	
		return null; //Não autorizado
		
	}
	
}
