package dev.besi.smarthome.backend.services

import dev.besi.smarthome.backend.firestore.Device
import dev.besi.smarthome.backend.firestore.FirestoreConstants.DEVICES_COLLECTION
import dev.besi.smarthome.backend.model.DeviceModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeviceService(
		@Autowired val utilsService: UtilsService
) {

	fun createDevice(deviceModel: DeviceModel): Device? =
			utilsService.createDocumentInCollectionWithContent(
					DEVICES_COLLECTION,
					6,
					Device(type = deviceModel.type),
					Device::class.java
			)

}
