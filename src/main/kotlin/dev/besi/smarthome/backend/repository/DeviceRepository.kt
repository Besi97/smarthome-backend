package dev.besi.smarthome.backend.repository

import dev.besi.smarthome.backend.repository.entities.Device
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository

interface DeviceRepository : FirestoreReactiveRepository<Device>
