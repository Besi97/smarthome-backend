package dev.besi.smarthome.backend.services

import dev.besi.smarthome.backend.exception.FailedToFindSuitableIdException
import dev.besi.smarthome.backend.exception.UserDoesNotOwnResourceException
import dev.besi.smarthome.backend.repository.DeviceRepository
import dev.besi.smarthome.backend.repository.HouseholdRepository
import dev.besi.smarthome.backend.repository.entities.Device
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeviceService(
		@Autowired val utilsService: UtilsService,
		@Autowired val deviceRepository: DeviceRepository,
		@Autowired val householdRepository: HouseholdRepository
) {

	@Throws(FailedToFindSuitableIdException::class)
	fun createDeviceWithType(deviceType: Device.DeviceType): Device? =
			utilsService.createDocumentWithContent(6, Device(type = deviceType), deviceRepository)

	fun updateDeviceName(userId: String, deviceId: String, name: String): Device? =
			deviceRepository.findById(deviceId).block()?.let { device ->
				if (device.householdId.isNullOrBlank())
					throw UserDoesNotOwnResourceException()

				val household = householdRepository.findById(device.householdId).block()
				if (household == null || household.userIds.contains(userId).not())
					throw UserDoesNotOwnResourceException()

				val updated = device.copy(name = name)
				return deviceRepository.save(updated).block()
			}

}
