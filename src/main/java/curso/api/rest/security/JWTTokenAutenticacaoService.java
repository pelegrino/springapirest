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
		
		//Liberando resposta para portas diferentes que usam API
		liberacaoCors(response);
		
		//Escreve token como resposta no corpo do http
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
		
	}
	
	//Retorna o usuário validado com token ou caso não seja validado retorna null
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		//Pega o token enviado no cabeçalho http
		String token = request.getHeader(HEADER_STRING);
		
		try {
			
			if (token != null) {
				
				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
				
				//Faz a validação do token do usuário na requisição
				String user = Jwts.parser()
								.setSigningKey(SECRET)
								.parseClaimsJws(tokenLimpo)
								.getBody()
								.getSubject();
				
				if (user != null) {
					Usuario usuario = ApplicationContextLoad.getApplicationContext()
										.getBean(UsuarioRepository.class)
										.findUserByLogin(user);
					
					if (usuario != null) {
						
						if (tokenLimpo.equalsIgnoreCase(usuario.getToken())) {
						
							return new UsernamePasswordAuthenticationToken(
										usuario.getLogin(),
										usuario.getSenha(),
										usuario.getAuthorities());
						}
					} 
				}
			}
		
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			
			try {
				response.getOutputStream().println("Seu token está expirado, faça o login novamente.");
				
			} catch (IOException e1) {}
		
		}
	
		liberacaoCors(response);
		
		return null; //Não autorizado
		
	}

	private void liberacaoCors(HttpServletResponse response) {
		
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
		
	}
	
}
