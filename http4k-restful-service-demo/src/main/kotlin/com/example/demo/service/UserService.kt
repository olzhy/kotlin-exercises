package com.example.demo.service

import com.example.demo.model.User

interface UserService {
    fun listAll(): List<User>
    fun getUserById(id: Long): User?
    fun update(user: User)
    fun save(user: User)
    fun deleteById(id: Long)
}

class DefaultUserServiceImpl : UserService {
    private val fakeUsers = mutableListOf(
            User(id = 1L, name = "Larry", age = 28),
            User(id = 2L, name = "Stephen", age = 19),
            User(id = 3L, name = "Jacky", age = 24)
    )

    override fun listAll(): List<User> {
        return fakeUsers
    }

    override fun getUserById(id: Long): User? {
        return fakeUsers.find { it.id == id }
    }

    override fun update(user: User) {
        fakeUsers.filter { it.id == user.id }.forEach {
            it.name = user.name
            it.age = user.age
        }
    }

    override fun save(user: User) {
        getUserById(user.id) ?: fakeUsers.add(user)
    }

    override fun deleteById(id: Long) {
        fakeUsers.removeIf { it.id == id }
    }
}