package dev.besi.smarthome.backend.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@PropertySource(value = ["application.yml"])
class CorsConfigurer : WebMvcConfigurer {
	@Value("\${cors.origin}")
	private lateinit var origin: String

	override fun addCorsMappings(registry: CorsRegistry) {
		registry.addMapping("/**")
				.allowedMethods("*")
				.allowedOrigins(origin)
				.allowCredentials(true)
	}
}
