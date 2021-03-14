package dev.besi.smarthome.backend.controllers.admin

import dev.besi.smarthome.backend.config.SecurityConst
import dev.besi.smarthome.backend.firestore.Device
import dev.besi.smarthome.backend.model.DeviceModel
import dev.besi.smarthome.backend.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["admin/devices"])
@PreAuthorize("hasAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
class DevicesController(
		@Autowired val deviceService: DeviceService
) {

	@PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
	fun postDevice(@RequestBody deviceModel: DeviceModel): Device =
			deviceService.createDevice(deviceModel) ?: Device()

}
