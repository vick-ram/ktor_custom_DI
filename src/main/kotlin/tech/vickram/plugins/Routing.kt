package tech.vickram.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import tech.vickram.models.DI
import tech.vickram.models.User

fun Application.configureRouting() {
    val userRepository = DI.userRepository

    routing {
        route("/users") {
            post {
                val user = call.receive<User>()
                val createdUser = userRepository.create(user)
                call.respond(HttpStatusCode.Created, createdUser)
            }

            get("/{id}") {
                try {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Invalid user ID")
                    val user = userRepository.read(id)
                    if (user != null) {
                        call.respond(user)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } catch (e: Exception) {
                    call.respond(e.message.toString())
                }
            }

            get {
                val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val queryParams = call.request.queryParameters.toMap().mapValues { it.value.firstOrNull() ?: "" }
                    .filterKeys { it != "offset" && it != "limit" }

                val users = userRepository.readAll(offset, limit, queryParams)
                call.respond(users)
            }

            put("/{id}") {
                try {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Invalid user ID")
                    val userUpdates = call.receive<User>()
                    val updatedUser = userRepository.update(id, userUpdates)
                    if (updatedUser != null) {
                        call.respond(updatedUser)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } catch (e: Exception) {
                    call.respond(e.message.toString())
                }
            }

            delete("/{id}") {
                try {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Invalid user ID")
                    val success = userRepository.delete(id)
                    if (success) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } catch (e: Exception) {
                    call.respond(e.message.toString())
                }
            }
        }
    }
}
