package dev.besi.smarthome.backend.model

import dev.besi.smarthome.backend.repository.entities.Device

data class AdminDevicesControllerPostCreateDeviceRequestModel(
		val type: Device.DeviceType
)
