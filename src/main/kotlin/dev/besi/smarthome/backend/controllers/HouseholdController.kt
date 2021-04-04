package dev.besi.smarthome.backend.controllers

import dev.besi.smarthome.backend.exception.*
import dev.besi.smarthome.backend.repository.entities.Household
import dev.besi.smarthome.backend.model.HouseholdControllerPostAddDeviceToHousehold
import dev.besi.smarthome.backend.model.HouseholdControllerPostHouseholdRequestModel
import dev.besi.smarthome.backend.model.HouseholdControllerPutUpdateHouseholdNameRequestModel
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

	@PutMapping(
			path = ["{householdId: [a-zA-Z0-9]{6}}/name"],
			consumes = [MediaType.APPLICATION_JSON_VALUE],
			produces = [MediaType.APPLICATION_JSON_VALUE]
	)
	fun updateHouseholdName(
			@PathVariable householdId: String,
			@AuthenticationPrincipal jwt: Jwt,
			@RequestBody householdName: HouseholdControllerPutUpdateHouseholdNameRequestModel
	): Household? =
			try {
				householdService.updateHouseholdName(jwt.subject, householdId, householdName.name)
			} catch (e: UserDoesNotOwnResourceException) {
				throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
			}

	@PutMapping(
			path = ["{householdId: [a-zA-Z0-9]{6}}/devices"],
			consumes = [MediaType.APPLICATION_JSON_VALUE],
			produces = [MediaType.APPLICATION_JSON_VALUE]
	)
	fun addDeviceToHousehold(
			@PathVariable householdId: String,
			@RequestBody deviceModel: HouseholdControllerPostAddDeviceToHousehold,
			@AuthenticationPrincipal jwt: Jwt
	): Household? =
			try {
				householdService.addDeviceToHousehold(deviceModel.deviceId, householdId, jwt.subject)
			} catch (e: UserDoesNotOwnResourceException) {
				throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
			}catch (e: DeviceAlreadyConnectedException) {
				throw ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"This device is already used in a household"
				)
			}

}
