package dev.besi.smarthome.backend.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
class SecurityConfig: WebSecurityConfigurerAdapter() {
	override fun configure(http: HttpSecurity?) {
		http!!
				.cors().and()
				.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.authorizeRequests().anyRequest().authenticated()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().oauth2ResourceServer().jwt()
	}
}
