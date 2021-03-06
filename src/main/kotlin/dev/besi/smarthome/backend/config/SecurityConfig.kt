package dev.besi.smarthome.backend.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

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
				.and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(
						JwtAuthenticationConverter().apply {
							setJwtGrantedAuthoritiesConverter(JwtGrantedAuthoritiesConverter().apply {
								setAuthoritiesClaimName(SecurityConst.JWT_SCOPES_KEY)
								setAuthorityPrefix("")
							})
						}
				)
	}
}
