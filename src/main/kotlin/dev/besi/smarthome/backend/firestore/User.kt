package dev.besi.smarthome.backend.firestore

import com.google.cloud.firestore.annotation.DocumentId

data class User(
		@DocumentId val id: String? = null,
		val householdIds: List<String>? = null
)
