package dev.besi.smarthome.backend.model

import com.google.firebase.auth.ListUsersPage

data class FirebaseUserPageModel(
        val nextPageToken: String,
        val users: List<FirebaseUserModel>
) {
    constructor(usersPage: ListUsersPage) : this(
            usersPage.nextPageToken,
            usersPage.values.map { userRecord -> FirebaseUserModel(userRecord) }
    )
}
