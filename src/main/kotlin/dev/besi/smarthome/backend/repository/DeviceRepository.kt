package dev.besi.smarthome.backend.repository

import dev.besi.smarthome.backend.repository.entities.Device
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository

interface DeviceRepository : FirestoreReactiveRepository<Device>
