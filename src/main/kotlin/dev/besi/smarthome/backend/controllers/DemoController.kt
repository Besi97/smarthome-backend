package dev.besi.smarthome.backend.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController {
    @GetMapping("/demo")
    fun getDemo() = "{\"text\": \"Hello World\"}"
}
