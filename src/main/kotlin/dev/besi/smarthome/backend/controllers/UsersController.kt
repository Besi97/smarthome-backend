package dev.besi.smarthome.backend.controllers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.FirestoreClient
import dev.besi.smarthome.backend.config.SecurityConst
import dev.besi.smarthome.backend.repository.entities.User
import dev.besi.smarthome.backend.model.FirebaseUserModel
import dev.besi.smarthome.backend.model.FirebaseUserPageModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["users"])
@PreAuthorize("hasAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
class UsersController {

	@GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
	fun getUsers(@RequestParam(required = false) nextPageToken: String?): FirebaseUserPageModel =
			FirebaseUserPageModel(FirebaseAuth.getInstance().listUsers(nextPageToken))

	@GetMapping(
			path = ["{id: [a-zA-Z0-9]{28}}"],
			produces = [MediaType.APPLICATION_JSON_VALUE]
	)
	fun getUser(@PathVariable id: String): FirebaseUserModel =
			try {
				FirebaseUserModel(FirebaseAuth.getInstance().getUser(id))
			} catch (e: Exception) {
				throw ResponseStatusException(HttpStatus.NOT_FOUND)
			}

	@GetMapping(
			path = ["{id: [a-zA-Z0-9]{28}}/claims"],
			produces = [MediaType.APPLICATION_JSON_VALUE]
	)
	fun getUserClaims(@PathVariable id: String): Map<String, Any> =
			try {
				FirebaseAuth.getInstance().getUser(id)
			} catch (e: Exception) {
				throw ResponseStatusException(HttpStatus.NOT_FOUND)
			}.customClaims

	@PostMapping(
			path = ["{id: [a-zA-Z0-9]{28}}/claims"],
			consumes = [MediaType.APPLICATION_JSON_VALUE],
			produces = [MediaType.APPLICATION_JSON_VALUE]
	)
	fun postUserClaims(@PathVariable id: String, @RequestBody claims: Map<String, Any>) =
			try {
				FirebaseAuth.getInstance().setCustomUserClaims(id, claims)
			} catch (e: IllegalArgumentException) {
				throw ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"Claims payload is invalid or too large"
				)
			}

}
