package dev.besi.smarthome.backend.model

import dev.besi.smarthome.backend.repository.entities.Device

data class DevicesControllerPostCreateDeviceRequestModel(
		val type: Device.DeviceType
)
