package org.covid19app.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@EnableJpaRepositories
@SpringBootApplication
class ServerApplication

@CrossOrigin
@RestController
class HealthController() {
	@GetMapping("/health")
	fun health(): String {
		return "HEALTHY"
	}
}

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)
}
