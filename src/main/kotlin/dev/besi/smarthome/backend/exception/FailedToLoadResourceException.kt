package dev.besi.smarthome.backend.exception

class FailedToLoadResourceException(
		val resource: String
) : Exception("Failed to load $resource")
