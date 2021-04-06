package dev.besi.smarthome.backend.controllers

import dev.besi.smarthome.backend.config.SecurityConst
import dev.besi.smarthome.backend.exception.FailedToFindSuitableIdException
import dev.besi.smarthome.backend.repository.entities.Device
import dev.besi.smarthome.backend.model.DevicesControllerPostCreateDeviceRequestModel
import dev.besi.smarthome.backend.model.StringWrapper
import dev.besi.smarthome.backend.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["devices"])
class DevicesController(
		@Autowired val deviceService: DeviceService
) {

	@PreAuthorize("hasAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
	@PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
	fun postDevice(@RequestBody device: DevicesControllerPostCreateDeviceRequestModel): Device? =
			try {
				deviceService.createDeviceWithType(device.type)
			} catch (e: FailedToFindSuitableIdException) {
				throw ResponseStatusException(
						HttpStatus.INTERNAL_SERVER_ERROR,
						"Failed to create device: can not find suitable id"
				)
			}

	@PutMapping(
			path = ["{deviceId: [a-zA-Z0-9]{6}}/name"],
			consumes = [MediaType.APPLICATION_JSON_VALUE],
			produces = [MediaType.APPLICATION_JSON_VALUE]
	)
	fun updateDeviceName(
			@RequestBody deviceName: StringWrapper,
			@PathVariable deviceId: String,
			@AuthenticationPrincipal jwt: Jwt
	): Device? =
			deviceService.updateDeviceName(jwt.subject, deviceId, deviceName.data)

}
