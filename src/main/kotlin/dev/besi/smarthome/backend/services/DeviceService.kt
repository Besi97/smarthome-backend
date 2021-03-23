package dev.besi.smarthome.backend.services

import dev.besi.smarthome.backend.exception.FailedToFindSuitableIdException
import dev.besi.smarthome.backend.repository.DeviceRepository
import dev.besi.smarthome.backend.repository.entities.Device
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeviceService(
		@Autowired val utilsService: UtilsService,
		@Autowired val deviceRepository: DeviceRepository
) {

	@Throws(FailedToFindSuitableIdException::class)
	fun createDeviceWithType(deviceType: Device.DeviceType): Device? =
			utilsService.createDocumentWithContent(6, Device(type = deviceType), deviceRepository)

	fun updateDeviceName(id: String, name: String): Device? =
			deviceRepository.findById(id).block()?.let { device ->
				val updated = device.copy(name = name)
				return deviceRepository.save(updated).block()
			}

}
