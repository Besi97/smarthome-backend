package dev.besi.smarthome.backend.services

import dev.besi.smarthome.backend.repository.UserRepository
import dev.besi.smarthome.backend.repository.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
		@Autowired val userRepository: UserRepository
) {

	private fun createDefaultUserDocument(id: String): User? =
			userRepository.save(User(id)).block()

	fun getUserDocument(userId: String): User? =
			userRepository.findById(userId).block()
					?: createDefaultUserDocument(userId)

}
