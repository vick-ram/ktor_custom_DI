package tech.vickram.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import tech.vickram.db.Users

object Databases {
    fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(Users)
        }
    }
}


private fun hikari(): HikariDataSource {
    val config = HikariConfig().apply {
        this.password = System.getenv("DB_PASSWORD")
        this.driverClassName = System.getenv("DB_DRIVER")
        this.jdbcUrl = System.getenv("DB_URL")
        this.username = System.getenv("DB_USER")
        this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    }
    return HikariDataSource(config)
}