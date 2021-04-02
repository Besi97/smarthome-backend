package dev.besi.smarthome.backend.services

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository
import dev.besi.smarthome.backend.exception.FailedToFindSuitableIdException
import org.springframework.stereotype.Service

@Service
class UtilsService {

	companion object {
		const val ID_GENERATION_SOURCE_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		const val ID_GENERATION_MAX_ITERATION_COUNT = 15
	}

	fun generateRandomId(length: Int): String {
		var id = ""
		for (i in 0..length) {
			id += ID_GENERATION_SOURCE_CHARSET.random()
		}
		return id
	}

	@Throws(FailedToFindSuitableIdException::class)
	fun <T : Any> createDocumentWithContent(
			idLength: Int,
			content: T,
			repository: FirestoreReactiveRepository<T>
	): T? {
		var id: String
		var count = 0
		do {
			id = generateRandomId(idLength)
			if(++count > ID_GENERATION_MAX_ITERATION_COUNT){
				throw FailedToFindSuitableIdException()
			}
		} while (!repository.existsById(id).block()!!)

		return repository.save(content).block()
	}

}
