package com.example.demo.conf

import com.example.demo.service.DefaultUserServiceImpl
import com.example.demo.service.UserService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val kodein = DI {
    bind<UserService>() with singleton { DefaultUserServiceImpl() }
}