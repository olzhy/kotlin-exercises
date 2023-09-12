package com.example.demo.dao

import com.example.demo.model.User
import org.apache.ibatis.annotations.*

@Mapper
interface UserMapper {

    @Select("SELECT id, name, age FROM user")
    fun listAll(): List<User>

    @Select("SELECT id, name, age FROM user WHERE id = #{id}")
    fun getById(id: Long): User?

    @Update("UPDATE user SET name = #{name}, age = #{age} WHERE id = #{id}")
    fun update(user: User)

    @Insert("INSERT INTO user(name, age) VALUES(#{name}, #{age})")
    fun save(user: User)

    @Delete("DELETE FROM user WHERE id = #{id}")
    fun deleteById(id: Long)

}