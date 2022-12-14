package com.example.myfirstservice

import org.slf4j.Logger
import org.slf4j.LoggerFactory.*
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/first-service")
class FirstServiceController(
    private val env: Environment
) {
    val log: Logger = getLogger(FirstServiceController::class.java)

    @GetMapping("/welcome")
    fun welcome() = "Welcome to my first service!"

    @GetMapping("/message")
    fun message(@RequestHeader("first-request") header: String): String {
        log.info("header: $header")
        return "Hello World in First Service"
    }

    /**
     * Round Robin
     */
    @GetMapping("/check")
    fun check(): String {
        return "Hi there. This is a message from First Service:${env.getProperty("local.server.port")}"
    }

}
