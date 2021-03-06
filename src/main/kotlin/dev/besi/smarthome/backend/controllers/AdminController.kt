package dev.besi.smarthome.backend.controllers

import com.google.firebase.auth.FirebaseAuth
import dev.besi.smarthome.backend.config.SecurityConst
import dev.besi.smarthome.backend.model.FirebaseUserModel
import dev.besi.smarthome.backend.model.FirebaseUserPageModel
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["admin"])
class AdminController {

    @GetMapping(path = ["users"])
    @PreAuthorize("hasAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
    fun getUsers(): FirebaseUserPageModel =
            FirebaseUserPageModel(FirebaseAuth.getInstance().listUsers(null))

    @GetMapping(path = ["user/{id}"])
    @PreAuthorize("hasAnyAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
    fun getUser(@PathVariable id: String): FirebaseUserModel =
            FirebaseUserModel(FirebaseAuth.getInstance().getUser(id))

    @GetMapping(path = ["user/{id}/claims"])
    @PreAuthorize("hasAnyAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
    fun getUserClaims(@PathVariable id: String): Map<String, Any> =
            FirebaseAuth.getInstance().getUser(id).customClaims

    @PostMapping(path = ["user/{id}/claims"])
    @PreAuthorize("hasAnyAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
    fun postUserClaims(@PathVariable id: String, @RequestBody claims: Map<String, Any>) =
            FirebaseAuth.getInstance().setCustomUserClaims(id, claims)

}
