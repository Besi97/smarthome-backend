package dev.besi.smarthome.backend.controllers.admin

import com.google.firebase.cloud.FirestoreClient
import dev.besi.smarthome.backend.config.SecurityConst
import dev.besi.smarthome.backend.firestore.Device
import dev.besi.smarthome.backend.firestore.FirestoreConstants.DEVICES_COLLECTION
import dev.besi.smarthome.backend.model.DeviceModel
import dev.besi.smarthome.backend.model.DevicesPageModel
import dev.besi.smarthome.backend.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["admin/devices"])
@PreAuthorize("hasAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
class DevicesController(
		@Autowired val deviceService: DeviceService
) {

	companion object {
		private const val DEVICES_PAGE_SIZE = 100
	}

	@GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
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

	@PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
	fun postDevice(@RequestBody deviceModel: DeviceModel): Device =
			deviceService.createDevice(deviceModel) ?: Device()

}
