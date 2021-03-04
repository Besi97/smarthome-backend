package dev.besi.smarthome.backend.controllers

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["admin"])
class AdminController {

    @GetMapping(path = ["test"])
    @Secured("SYS_ADMIN")
    fun getTest() = "{\"text\": \"Test passed\"}"

    @GetMapping(path = ["{id}"])
    fun setAdmin(@PathVariable id: String) {
        FirebaseAuth.getInstance().setCustomUserClaims(id, mapOf("scope" to arrayOf("SYS_ADMIN")))
    }

}
