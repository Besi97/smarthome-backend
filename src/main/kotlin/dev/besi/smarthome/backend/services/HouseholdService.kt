package dev.besi.smarthome.backend.services

import com.google.firebase.cloud.FirestoreClient
import dev.besi.smarthome.backend.exception.*
import dev.besi.smarthome.backend.firestore.Device
import dev.besi.smarthome.backend.firestore.FirestoreConstants.DEVICES_COLLECTION
import dev.besi.smarthome.backend.firestore.FirestoreConstants.HOUSEHOLDS_COLLECTION
import dev.besi.smarthome.backend.firestore.FirestoreConstants.USERS_COLLECTION
import dev.besi.smarthome.backend.firestore.Household
import dev.besi.smarthome.backend.firestore.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HouseholdService(
		@Autowired val utilsService: UtilsService,
		@Autowired val deviceService: DeviceService
) {

	@Throws(FailedToFindSuitableIdException::class, FailedToCreateDocumentException::class)
	fun createHousehold(name: String, userId: String): Household =
			utilsService.createDocumentInCollectionWithContent(
					HOUSEHOLDS_COLLECTION,
					5,
					Household(name = name, userIds = listOf(userId)),
					Household::class.java
			) { household, transaction ->
				FirestoreClient.getFirestore().collection(USERS_COLLECTION).document(userId).let { userDoc ->
					val user = transaction.get(userDoc).get().toObject(User::class.java)!!
					val updated = user.copy(householdIds = listOf(*user.householdIds!!.toTypedArray(), household.id!!))
					transaction.set(userDoc, updated)
				}
			}

	@Throws(
			FailedToLoadResourceException::class,
			UserDoesNotOwnHouseholdException::class,
			DeviceAlreadyConnectedException::class
	)
	fun addDeviceToHousehold(householdId: String, deviceId: String, userId: String): Household {
		val household = FirestoreClient.getFirestore().collection(HOUSEHOLDS_COLLECTION).document(householdId)
				.get().get().toObject(Household::class.java) ?: throw FailedToLoadResourceException("household")
		if (household.userIds == null || household.userIds.contains(userId).not())
			throw UserDoesNotOwnHouseholdException()
		val device = deviceService.getDevice(deviceId) ?: throw  FailedToLoadResourceException("device")
		if (device.householdId != null && device.householdId.isNotBlank())
			throw DeviceAlreadyConnectedException()
		connectDeviceToHousehold(household, device)
		return FirestoreClient.getFirestore().collection(HOUSEHOLDS_COLLECTION).document(householdId)
				.get().get().toObject(Household::class.java) ?: throw FailedToLoadResourceException("household")
	}

	private fun connectDeviceToHousehold(household: Household, device: Device): Boolean {
		val updatedDevice = device.copy(householdId = household.id)
		val updatedHousehold = household.copy(deviceIds = listOf(*household.deviceIds!!.toTypedArray(), device.id!!))
		FirestoreClient.getFirestore().let { firestore ->
			val deviceDocument = firestore.collection(DEVICES_COLLECTION).document(device.id)
			val householdDocument = firestore.collection(HOUSEHOLDS_COLLECTION).document(household.id!!)
			firestore.runTransaction { transaction ->
				transaction.set(deviceDocument, updatedDevice)
				transaction.set(householdDocument, updatedHousehold)
			}.get()
		}
		return true
	}

}
