package dev.besi.smarthome.backend.services

import dev.besi.smarthome.backend.exception.FailedToCreateDocumentException
import dev.besi.smarthome.backend.exception.FailedToFindSuitableIdException
import dev.besi.smarthome.backend.firestore.Device
import dev.besi.smarthome.backend.firestore.FirestoreConstants.DEVICES_COLLECTION
import dev.besi.smarthome.backend.model.AdminDevicesControllerPostCreateDeviceRequestModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeviceService(
		@Autowired val utilsService: UtilsService
) {

	@Throws(FailedToFindSuitableIdException::class, FailedToCreateDocumentException::class)
	fun createDevice(device: AdminDevicesControllerPostCreateDeviceRequestModel): Device =
			utilsService.createDocumentInCollectionWithContent(
					DEVICES_COLLECTION,
					6,
					Device(type = device.type),
					Device::class.java
			)

}
