package dev.besi.smarthome.backend.services

import com.google.firebase.cloud.FirestoreClient
import dev.besi.smarthome.backend.firestore.Device
import dev.besi.smarthome.backend.firestore.FirestoreConstants.DEVICES_COLLECTION
import dev.besi.smarthome.backend.model.DeviceModel
import org.springframework.stereotype.Service

@Service
class DeviceService {

	private fun generateDeviceId(): String {
		val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		var id = ""
		for (i in 0..6) {
			id += chars.random()
		}
		return id
	}

	fun createDevice(deviceModel: DeviceModel): Device? {
		val collection = FirestoreClient.getFirestore().collection(DEVICES_COLLECTION)
		var id: String
		do {
			id = generateDeviceId()
		} while(collection.document(id).get().get().exists().not())
		collection.document(id).set(Device(id = id, type = deviceModel.type)).get()
		return collection.document(id).get().get().toObject(Device::class.java)
	}

}
