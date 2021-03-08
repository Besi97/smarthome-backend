package dev.besi.smarthome.backend.model

import dev.besi.smarthome.backend.firestore.Device

data class DeviceModel(
		val type: Device.DeviceType
)
