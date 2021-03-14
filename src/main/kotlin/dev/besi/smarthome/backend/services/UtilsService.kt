package dev.besi.smarthome.backend.services

import com.google.firebase.cloud.FirestoreClient
import org.springframework.stereotype.Service

@Service
class UtilsService {

	companion object {
		const val ID_GENERATION_MAX_ITERATION_COUNT = 15
	}

	fun generateRandomId(length: Int): String {
		val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		var id = ""
		for (i in 0..length) {
			id += chars.random()
		}
		return id
	}

	fun <T : Any> createDocumentInCollectionWithContent(collectionName: String, idLength: Int, content: T, ofClass: Class<T>): T? =
			FirestoreClient.getFirestore().collection(collectionName).let { collection ->
				var id: String
				var count = 0
				do {
					id = generateRandomId(idLength)
					if (++count > ID_GENERATION_MAX_ITERATION_COUNT) {
						return null
					}
				} while (collection.document(id).get().get().exists())
				collection.document(id).let { document ->
					document.set(content)
					document.get().get().toObject(ofClass)
				}
			}
}
