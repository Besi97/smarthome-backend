package dev.besi.smarthome.backend.services

import dev.besi.smarthome.backend.exception.FailedToCreateHouseholdException
import dev.besi.smarthome.backend.exception.FailedToFindSuitableIdException
import dev.besi.smarthome.backend.repository.HouseholdRepository
import dev.besi.smarthome.backend.repository.UserRepository
import dev.besi.smarthome.backend.repository.entities.Household
import dev.besi.smarthome.backend.repository.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HouseholdService(
		@Autowired val utilsService: UtilsService,
		@Autowired val householdRepository: HouseholdRepository,
		@Autowired val userRepository: UserRepository,
		@Autowired val userService: UserService
) {

	@Transactional
	@Throws(FailedToFindSuitableIdException::class, FailedToCreateHouseholdException::class)
	fun createHousehold(name: String, userId: String): Household? =
			utilsService.createDocumentWithContent(
					6, Household(name = name, userIds = listOf(userId)), householdRepository
			)?.also { household ->
				userRepository.findById(userId).block()
						?.let { user ->
							val updatedUser = user.copy(householdIds = listOf(*user.householdIds.toTypedArray(), household.id!!))
							userRepository.save(updatedUser)
						}
						?: userService.createUserDocument(User(id = userId, householdIds = listOf(household.id!!)))
						?: throw FailedToCreateHouseholdException()
			}

	fun updateHouseholdName(id: String, name: String): Household? =
			householdRepository.findById(id).block()?.let { household ->
				val updated = household.copy(name = name)
				return householdRepository.save(updated).block()
			}

	fun addDeviceToHousehold(deviceId: String, householdId: String, userId: String): Household? =
			TODO()

}
