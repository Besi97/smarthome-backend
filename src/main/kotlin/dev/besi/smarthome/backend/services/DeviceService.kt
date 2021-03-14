package dev.besi.smarthome.backend.services

import com.google.firebase.cloud.FirestoreClient
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

	/**
	 * @return null, if device can not be found by id
	 */
	fun getDevice(deviceId: String): Device? =
			FirestoreClient.getFirestore().collection(DEVICES_COLLECTION).document(deviceId)
					.get().get().toObject(Device::class.java)

}
