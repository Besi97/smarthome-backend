package dev.besi.smarthome.backend.controllers.admin

import com.google.firebase.cloud.FirestoreClient
import dev.besi.smarthome.backend.config.SecurityConst
import dev.besi.smarthome.backend.firestore.Device
import dev.besi.smarthome.backend.firestore.FirestoreConstants.DEVICES_COLLECTION
import dev.besi.smarthome.backend.model.DeviceModel
import dev.besi.smarthome.backend.model.DevicesPageModel
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["admin/devices"])
@PreAuthorize("hasAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
class DevicesController {

	companion object {
		private const val DEVICES_PAGE_SIZE = 100
	}

	@GetMapping
	fun getDevices(@RequestParam(required = false) startAfterId: String?): DevicesPageModel =
			DevicesPageModel(
					FirestoreClient.getFirestore().collection(DEVICES_COLLECTION).let {
						(if (startAfterId == null) {
							it
						} else {
							it.startAfter(it.document(startAfterId).get().get())
						}).limit(DEVICES_PAGE_SIZE).get().get().toObjects(Device::class.java)
					}
			)

	@PostMapping
	fun postDevice(@RequestBody deviceModel: DeviceModel): Device =
		FirestoreClient.getFirestore().collection(DEVICES_COLLECTION).add(Device(deviceModel))
				.get().get().get().toObject(Device::class.java)
				?: Device()

}
