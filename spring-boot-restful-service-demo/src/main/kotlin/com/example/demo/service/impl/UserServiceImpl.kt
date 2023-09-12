package com.example.demo.service.impl

import com.example.demo.dao.UserMapper
import com.example.demo.model.User
import com.example.demo.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(val userMapper: UserMapper) : UserService {

    override fun listAll(): List<User> = userMapper.listAll()

    override fun getById(id: Long): User? = userMapper.getById(id)

    override fun update(user: User) = userMapper.update(user)

    override fun save(user: User) = userMapper.save(user)

    override fun deleteById(id: Long) = userMapper.deleteById(id)

}