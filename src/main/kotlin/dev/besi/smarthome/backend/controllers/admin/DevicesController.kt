package dev.besi.smarthome.backend.controllers.admin

import dev.besi.smarthome.backend.config.SecurityConst
import dev.besi.smarthome.backend.exception.FailedToCreateDocumentException
import dev.besi.smarthome.backend.exception.FailedToFindSuitableIdException
import dev.besi.smarthome.backend.firestore.Device
import dev.besi.smarthome.backend.model.AdminDevicesControllerPostCreateDeviceRequestModel
import dev.besi.smarthome.backend.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["admin/devices"])
@PreAuthorize("hasAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
class DevicesController(
		@Autowired val deviceService: DeviceService
) {

	@PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
	fun postDevice(@RequestBody device: AdminDevicesControllerPostCreateDeviceRequestModel): Device =
			try {
				deviceService.createDevice(device)
			} catch (e: FailedToFindSuitableIdException) {
				throw ResponseStatusException(
						HttpStatus.INTERNAL_SERVER_ERROR,
						"Failed to create device: can not find suitable id"
				)
			} catch (e: FailedToCreateDocumentException) {
				throw ResponseStatusException(
						HttpStatus.INTERNAL_SERVER_ERROR,
						"Failed to create device: could not create document"
				)
			}

}
