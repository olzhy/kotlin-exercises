package com.example.demo.service

import com.example.demo.model.User

interface UserService {

    fun listAll(): List<User>

    fun getById(id: Long): User?

    fun update(user: User)

    fun save(user: User)

    fun deleteById(id: Long)

}