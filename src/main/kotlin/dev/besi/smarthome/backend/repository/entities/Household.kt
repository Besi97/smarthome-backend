package dev.besi.smarthome.backend.repository.entities

import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.spring.data.firestore.Document

@Document(collectionName = "households")
data class Household(
		@DocumentId val id: String? = null,
		val name: String? = id,
		val userIds: List<String> = listOf(),
		val deviceIds: List<String> = listOf()
)
