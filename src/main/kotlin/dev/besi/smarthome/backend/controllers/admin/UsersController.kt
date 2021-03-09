package dev.besi.smarthome.backend.controllers.admin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.FirestoreClient
import dev.besi.smarthome.backend.config.SecurityConst
import dev.besi.smarthome.backend.firestore.FirestoreConstants.USERS_COLLECTION
import dev.besi.smarthome.backend.firestore.User
import dev.besi.smarthome.backend.model.FirebaseUserModel
import dev.besi.smarthome.backend.model.FirebaseUserPageModel
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["admin/users"])
@PreAuthorize("hasAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
class UsersController {

    @GetMapping
    fun getUsers(@RequestParam(required = false) nextPageToken: String?): FirebaseUserPageModel =
            FirebaseUserPageModel(FirebaseAuth.getInstance().listUsers(nextPageToken))

    @GetMapping(path = ["{id}"])
    fun getUser(@PathVariable id: String): FirebaseUserModel =
            FirebaseUserModel(FirebaseAuth.getInstance().getUser(id))

    @GetMapping(path = ["{id}/claims"])
    fun getUserClaims(@PathVariable id: String): Map<String, Any> =
            FirebaseAuth.getInstance().getUser(id).customClaims

    @PostMapping(path = ["{id}/claims"])
    fun postUserClaims(@PathVariable id: String, @RequestBody claims: Map<String, Any>) =
            FirebaseAuth.getInstance().setCustomUserClaims(id, claims)

    @GetMapping(path = ["{id}/households"])
    fun getUserHouseholds(@PathVariable id: String): List<String> =
            FirestoreClient.getFirestore().collection(USERS_COLLECTION).document(id).get()
                    .get().toObject(User::class.java)!!.householdIds!!

}
