package com.example.demo.controller

import com.example.demo.model.User
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @GetMapping("/")
    fun listAll() = userService.listAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = userService.getById(id)

    @PatchMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@RequestBody user: User) = userService.update(user)

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody user: User) = userService.save(user)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable("id") id: Long) = userService.deleteById(id)

}