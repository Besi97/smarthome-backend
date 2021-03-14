package dev.besi.smarthome.backend.controllers

import dev.besi.smarthome.backend.firestore.Household
import dev.besi.smarthome.backend.services.HouseholdService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["households"])
class HouseholdController(
		@Autowired val householdService: HouseholdService
) {

	data class HouseholdPostModel(
			val name: String
	)

	data class AddDevicePostModel(
			val deviceId: String
	)

	@PostMapping
	fun createHousehold(@RequestBody householdModel: HouseholdPostModel, @AuthenticationPrincipal jwt: Jwt): Household =
			householdService.createHousehold(householdModel.name, jwt.subject) ?: Household()

	@PostMapping(path = ["{householdId}/devices"])
	fun addDeviceToHousehold(
			@PathVariable householdId: String,
			@RequestBody deviceModel: AddDevicePostModel,
			@AuthenticationPrincipal jwt: Jwt
	): Household =
			householdService.addDeviceToHousehold(householdId, deviceModel.deviceId, jwt.subject) ?: Household()

}
