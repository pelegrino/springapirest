package curso.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.api.rest.service.ImplementacaoUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	
	//Atentar que esse código está deprecado no Spring 6 em diante, em especial o security web e security config
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	        .disable()
	        .authorizeRequests()
	            .antMatchers("/").permitAll()
	            .anyRequest().authenticated()
	        .and()
	        .logout()
	            .logoutSuccessUrl("/index")
	            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(implementacaoUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	
	}
	
}
