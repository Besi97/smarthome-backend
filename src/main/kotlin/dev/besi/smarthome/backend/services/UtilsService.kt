package dev.besi.smarthome.backend.services

import com.google.cloud.firestore.Transaction
import com.google.firebase.cloud.FirestoreClient
import dev.besi.smarthome.backend.exception.FailedToCreateDocumentException
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

	@Throws(FailedToFindSuitableIdException::class, FailedToCreateDocumentException::class)
	fun <T : Any> createDocumentInCollectionWithContent(
			collectionName: String,
			idLength: Int,
			content: T,
			ofClass: Class<T>,
			runInTransaction: (created: T, transaction: Transaction) -> Unit = { _, _ -> }
	): T =
			FirestoreClient.getFirestore().collection(collectionName).let { collection ->
				var id: String
				var count = 0
				do {
					id = generateRandomId(idLength)
					if (++count > ID_GENERATION_MAX_ITERATION_COUNT) {
						throw FailedToFindSuitableIdException()
					}
				} while (collection.document(id).get().get().exists())
				collection.document(id).let { document ->
					FirestoreClient.getFirestore().runTransaction { transaction ->
						transaction.set(document, content)
						runInTransaction(transaction.get(document).get().toObject(ofClass)!!, transaction)
					}.get()
					document.get().get().toObject(ofClass)
				}
			} ?: throw FailedToCreateDocumentException()
}
