package com.example.userservice.user.controller

import com.example.userservice.Greeting
import com.example.userservice.user.domain.dto.CreateUserDto
import com.example.userservice.user.domain.dto.ResponseUserDto
import com.example.userservice.user.service.UserService
import io.micrometer.core.annotation.Timed
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val greeting: Greeting,
    private val userService: UserService,
    private val env: Environment,
) {

    private val log = org.slf4j.LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true)
    fun status(): String {
        val port = env.getProperty("local.server.port")
        return "It's Working in User Service on PORT $port" +
                ", with token = ${env.getProperty("token.secret")}" +
                ", with token expiration time = ${env.getProperty("token.expiration_time")}"
    }

    @GetMapping("/welcome")
    @Timed(value = "users.welcome", longTask = true)
    fun welcome(): String {
        return greeting.message
    }

    @PostMapping("/users")
    fun createUser(
        @RequestBody createUserDto: CreateUserDto
    ): ResponseEntity<ResponseUserDto> {
        val userDto = userService.createUser(createUserDto)
        val responseUser = ResponseUserDto.of(userDto)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseUser)
    }

    @GetMapping("/users/{userId}")
    fun findUser(
        @PathVariable userId: String
    ): ResponseEntity<ResponseUserDto> {
        val userDto = userService.findUser(userId)

        val responseUser = ResponseUserDto.of(userDto)
        return ResponseEntity.ok(responseUser)
    }

    @GetMapping("/users")
    fun findAllUsers(): ResponseEntity<Iterable<ResponseUserDto>> {
        val users = userService.findAllUsers()
        val responseUsers = users.map { ResponseUserDto.of(it.toDto(emptyList())) }
        return ResponseEntity.ok(responseUsers)
    }

}
