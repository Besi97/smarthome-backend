package dev.besi.smarthome.backend.controllers

import dev.besi.smarthome.backend.exception.*
import dev.besi.smarthome.backend.repository.entities.Household
import dev.besi.smarthome.backend.model.HouseholdControllerPostAddDeviceToHousehold
import dev.besi.smarthome.backend.model.HouseholdControllerPostHouseholdRequestModel
import dev.besi.smarthome.backend.services.HouseholdService
import dev.besi.smarthome.backend.services.UtilsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["households"])
class HouseholdController(
		@Autowired val householdService: HouseholdService
) {

	@PostMapping(
			consumes = [MediaType.APPLICATION_JSON_VALUE],
			produces = [MediaType.APPLICATION_JSON_VALUE]
	)
	fun createHousehold(
			@RequestBody householdModel: HouseholdControllerPostHouseholdRequestModel,
			@AuthenticationPrincipal jwt: Jwt
	): Household? =
			try {
				householdService.createHousehold(householdModel.name, jwt.subject)
			} catch (e: FailedToFindSuitableIdException) {
				throw ResponseStatusException(
						HttpStatus.INTERNAL_SERVER_ERROR,
						"Failed to create household: can not find suitable id"
				)
			}

	@PostMapping(
			path = ["{householdId: [" + UtilsService.ID_GENERATION_SOURCE_CHARSET + "]+}/devices"],
			consumes = [MediaType.APPLICATION_JSON_VALUE],
			produces = [MediaType.APPLICATION_JSON_VALUE]
	)
	fun addDeviceToHousehold(
			@PathVariable householdId: String,
			@RequestBody deviceModel: HouseholdControllerPostAddDeviceToHousehold,
			@AuthenticationPrincipal jwt: Jwt
	): Household? =
			try {
				householdService.addDeviceToHousehold(householdId, deviceModel.deviceId, jwt.subject)
			} catch (e: UserDoesNotOwnHouseholdException) {
				throw ResponseStatusException(
						HttpStatus.UNAUTHORIZED,
						"You are not allowed to edit this household"
				)
			} catch (e: DeviceAlreadyConnectedException) {
				throw ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"This device is already used in a household"
				)
			}

}
