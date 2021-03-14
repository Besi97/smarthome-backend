package dev.besi.smarthome.backend.services

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import org.springframework.stereotype.Service

@Service
class UtilsService {

	fun generateRandomId(length: Int): String {
		val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		var id = ""
		for (i in 0..length) {
			id += chars.random()
		}
		return id
	}

	fun <T: Any> createDocumentInCollectionWithContent(collectionName: String, idLength: Int, content: T, ofClass: Class<T>): T? =
			FirestoreClient.getFirestore().collection(collectionName).let { collection ->
				var id: String
				do {
					id = generateRandomId(idLength)
				} while (collection.document(id).get().get().exists().not())
				collection.document(id).let { document ->
					document.set(content)
					document.get().get().toObject(ofClass)
				}
			}
}
