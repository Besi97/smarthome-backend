package dev.besi.smarthome.backend.repository

import dev.besi.smarthome.backend.repository.entities.Household
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository

interface HouseholdRepository : FirestoreReactiveRepository<Household>
