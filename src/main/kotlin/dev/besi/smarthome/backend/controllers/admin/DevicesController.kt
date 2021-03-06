package dev.besi.smarthome.backend.controllers.admin

import dev.besi.smarthome.backend.config.SecurityConst
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["admin/devices"])
@PreAuthorize("hasAuthority('${SecurityConst.Scope.SYS_ADMIN}')")
class DevicesController {



}
