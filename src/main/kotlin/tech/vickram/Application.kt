package tech.vickram

import io.ktor.server.application.*
import tech.vickram.plugins.Databases
import tech.vickram.plugins.configureRouting
import tech.vickram.plugins.configureSerialization
import tech.vickram.plugins.configureStatusPages

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    Databases.init()
    configureSerialization()
    configureRouting()
    configureStatusPages()
}
