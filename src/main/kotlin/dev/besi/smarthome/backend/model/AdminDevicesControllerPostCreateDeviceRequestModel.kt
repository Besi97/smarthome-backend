package dev.besi.smarthome.backend.model

import dev.besi.smarthome.backend.firestore.Device

data class AdminDevicesControllerPostCreateDeviceRequestModel(
		val type: Device.DeviceType
)
