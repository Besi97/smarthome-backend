package dev.besi.smarthome.backend.firestore

import com.google.cloud.firestore.annotation.DocumentId

open class Device(
        @DocumentId val id: String? = null,
        val name: String? = null,
        val type: DeviceType? = null
) {
    enum class DeviceType {
        DHT_22, LED_DRIVER
    }
}
