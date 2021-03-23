package dev.besi.smarthome.backend.repository.entities

import com.google.cloud.firestore.annotation.DocumentId
import org.springframework.cloud.gcp.data.firestore.Document

@Document(collectionName = "devices")
data class Device(
		@DocumentId val id: String? = null,
		val name: String? = id,
		val type: DeviceType? = null,
		val householdId: String? = null
) {
	enum class DeviceType {
		DHT_22, LED_DRIVER
	}
}
