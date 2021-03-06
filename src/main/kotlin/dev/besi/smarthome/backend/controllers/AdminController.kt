package dev.besi.smarthome.backend.controllers

import com.google.firebase.auth.FirebaseAuth
import dev.besi.smarthome.backend.config.SecurityConst
import dev.besi.smarthome.backend.model.FirebaseUserModel
import dev.besi.smarthome.backend.model.FirebaseUserPageModel
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

}
