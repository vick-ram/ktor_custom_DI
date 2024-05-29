package tech.vickram.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("users") {
    val username = varchar("username", 250)
    val email = varchar("email", 250).uniqueIndex()
    val password = varchar("password", 200)
}

class UserEntity(userId: EntityID<Int>) : IntEntity(userId) {
    companion object : IntEntityClass<UserEntity>(Users)

    var username by Users.username
    var email by Users.email
    var password by Users.password

    fun toDomain() = tech.vickram.models.User(id.value, username, email, password)
}