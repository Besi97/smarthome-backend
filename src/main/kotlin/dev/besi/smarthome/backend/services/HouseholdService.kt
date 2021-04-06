package dev.besi.smarthome.backend.services

import com.google.firebase.auth.FirebaseAuth
import dev.besi.smarthome.backend.exception.*
import dev.besi.smarthome.backend.repository.DeviceRepository
import dev.besi.smarthome.backend.repository.HouseholdRepository
import dev.besi.smarthome.backend.repository.UserRepository
import dev.besi.smarthome.backend.repository.entities.Household
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HouseholdService(
		@Autowired val utilsService: UtilsService,
		@Autowired val householdRepository: HouseholdRepository,
		@Autowired val userRepository: UserRepository,
		@Autowired val deviceRepository: DeviceRepository,
		@Autowired val userService: UserService
) {

	@Transactional
	@Throws(FailedToFindSuitableIdException::class, FailedToCreateHouseholdException::class)
	fun createHousehold(name: String, userId: String): Household? =
			utilsService.createDocumentWithContent(
					6, Household(name = name, userIds = listOf(userId)), householdRepository
			)?.also { household ->
				userService.getUserDocument(userId)
						?.let { user ->
							val updatedUser = user.copy(householdIds = listOf(*user.householdIds.toTypedArray(), household.id!!))
							userRepository.save(updatedUser)
						}
						?: throw FailedToCreateHouseholdException()
			}

	@Throws(UserDoesNotOwnResourceException::class)
	fun updateHouseholdName(userId: String, householdId: String, name: String): Household? =
			householdRepository.findById(householdId).block()?.let { household ->
				if (household.userIds.contains(userId)) {
					val updated = household.copy(name = name)
					return householdRepository.save(updated).block()
				} else {
					throw UserDoesNotOwnResourceException()
				}
			}

	@Transactional
	@Throws(UserDoesNotOwnResourceException::class, DeviceAlreadyConnectedException::class)
	fun addDeviceToHousehold(deviceId: String, householdId: String, userId: String): Household? {
		val device = deviceRepository.findById(deviceId).block()
		val household = householdRepository.findById(householdId).block()
		if (device == null || household == null)
			return null
		if (household.userIds.contains(userId).not())
			throw UserDoesNotOwnResourceException()
		if (device.householdId.isNullOrBlank().not())
			throw DeviceAlreadyConnectedException()

		val updatedDevice = device.copy(householdId = householdId)
		val updatedHousehold = household.copy(deviceIds = listOf(*household.deviceIds.toTypedArray(), deviceId))
		deviceRepository.save(updatedDevice).block()
		return householdRepository.save(updatedHousehold).block()
	}

	@Transactional
	@Throws(UserDoesNotOwnResourceException::class)
	fun removeDeviceFromHousehold(deviceId: String, householdId: String, userId: String): Household? {
		val device = deviceRepository.findById(deviceId).block() ?: return null
		val household = householdRepository.findById(householdId).block() ?: return null
		if (!household.userIds.contains(userId))
			throw UserDoesNotOwnResourceException()
		if (device.householdId != householdId)
			return null

		val updatedDevice = device.copy(householdId = null)
		val updatedHousehold = household.copy(deviceIds = listOf(
				*household.deviceIds.filterNot { it == deviceId }.toTypedArray()
		))
		deviceRepository.save(updatedDevice).block()
		return householdRepository.save(updatedHousehold).block()
	}

	@Transactional
	@Throws(UserDoesNotOwnResourceException::class, UserNotFoundException::class)
	fun addUserToHousehold(householdId: String, userEmail: String, ownerUserId: String): Household? {
		val household = householdRepository.findById(householdId).block() ?: return null
		if (!household.userIds.contains(ownerUserId))
			throw UserDoesNotOwnResourceException()
		val user = FirebaseAuth.getInstance().getUserByEmail(userEmail).uid.let { userId ->
			userRepository.findById(userId).block()
		} ?: throw UserNotFoundException()

		val updatedHousehold = household.copy(userIds = listOf(*household.userIds.toTypedArray(), user.id!!))
		val updatedUser = user.copy(householdIds = listOf(*user.householdIds.toTypedArray(), householdId))
		userRepository.save(updatedUser).block()
		return householdRepository.save(updatedHousehold).block()
	}

}
