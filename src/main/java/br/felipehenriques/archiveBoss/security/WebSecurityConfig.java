package br.felipehenriques.archiveBoss.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.felipehenriques.archiveBoss.security.jwt.JWTAuthenticationFilter;
import br.felipehenriques.archiveBoss.security.jwt.JWTLoginFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        // disable caching
        http.headers().cacheControl();

        http.csrf().disable() // disable csrf for our requests.
        	.headers().frameOptions().disable()
        	.and()
        	.authorizeRequests()
        	.antMatchers(HttpMethod.POST,"/users").permitAll()
        	.antMatchers("/h2/**").permitAll()
        	.anyRequest().authenticated()
        	.and()
        	// We filter the api/login requests
        	.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        	// And filter other requests to check the presence of JWT in header
        	.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
