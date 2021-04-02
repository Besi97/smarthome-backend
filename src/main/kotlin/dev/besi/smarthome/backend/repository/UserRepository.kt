package dev.besi.smarthome.backend.repository

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository
import dev.besi.smarthome.backend.repository.entities.User

interface UserRepository : FirestoreReactiveRepository<User>
