package dev.besi.smarthome.backend.firestore

import com.google.cloud.firestore.annotation.DocumentId

data class Household(
		@DocumentId val id: String? = null,
		val name: String? = null,
		val userIds: List<String>? = listOf(),
		val deviceIds: List<String>? = listOf()
)
