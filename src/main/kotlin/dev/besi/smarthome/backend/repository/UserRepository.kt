package dev.besi.smarthome.backend.repository

import dev.besi.smarthome.backend.repository.entities.User
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository

interface UserRepository : FirestoreReactiveRepository<User>
