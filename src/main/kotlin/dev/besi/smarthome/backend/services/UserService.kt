package dev.besi.smarthome.backend.services

import dev.besi.smarthome.backend.repository.UserRepository
import dev.besi.smarthome.backend.repository.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
		@Autowired val userRepository: UserRepository
) {

	fun createDefaultUserDocument(id: String): User? =
			userRepository.save(User(id)).block()

	fun createUserDocument(content: User): User? =
			userRepository.save(content).block()

}
