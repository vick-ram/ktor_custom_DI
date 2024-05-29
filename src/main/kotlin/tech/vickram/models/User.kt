package tech.vickram.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: Int? = null,
    val username: String,
    val email: String,
    val password: String
)
