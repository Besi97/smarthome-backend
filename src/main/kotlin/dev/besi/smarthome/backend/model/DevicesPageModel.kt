package dev.besi.smarthome.backend.model

import dev.besi.smarthome.backend.firestore.Device

data class DevicesPageModel(
		val nextPageToken: String,
		val devices: List<Device>
) {
	constructor(devices: List<Device>) : this(devices.takeLast(1)[0].id!!, devices)
}
