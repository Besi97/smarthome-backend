package dev.besi.smarthome.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class SmarthomeBackendApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
	runApplication<SmarthomeBackendApplication>(*args)
}
