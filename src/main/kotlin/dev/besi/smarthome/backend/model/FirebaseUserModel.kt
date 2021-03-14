package dev.besi.smarthome.backend.model

import com.google.firebase.auth.UserRecord
import dev.besi.smarthome.backend.config.SecurityConst

data class FirebaseUserModel(
		val uid: String,
		val displayName: String,
		val email: String,
		val photoUrl: String,
		val isEmailVerified: Boolean,
		val isDisabled: Boolean,
		val scope: List<String>
) {
	constructor(user: UserRecord) : this(
			user.uid,
			user.displayName,
			user.email,
			user.photoUrl,
			user.isEmailVerified,
			user.isDisabled,
			user.customClaims[SecurityConst.JWT_SCOPES_KEY]?.let { scope ->
				if (scope is Array<*>) scope.asList().filterIsInstance<String>() else listOf()
			} ?: listOf()
	)
}
