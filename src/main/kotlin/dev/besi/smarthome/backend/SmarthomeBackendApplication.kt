package dev.besi.smarthome.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class SmarthomeBackendApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
	runApplication<SmarthomeBackendApplication>(*args)
}
