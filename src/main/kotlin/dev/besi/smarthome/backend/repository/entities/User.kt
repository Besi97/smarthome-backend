package dev.besi.smarthome.backend.repository.entities

import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.spring.data.firestore.Document

@Document(collectionName = "users")
data class User(
		@DocumentId val id: String? = null,
		val householdIds: List<String> = listOf()
)
