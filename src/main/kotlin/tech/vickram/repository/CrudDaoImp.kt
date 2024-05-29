package tech.vickram.repository

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction

abstract class CrudRepositoryImpl<T : IntEntity, D>(private val entityClass: IntEntityClass<T>) : CrudRepository<D, Int> {
    abstract fun T.toDomainModel(): D
    abstract fun D.toEntity(entity: T)

    override fun create(entity: D): D = transaction {
        entityClass.new {
            entity.toEntity(this)
        }.toDomainModel()
    }

    override fun read(id: Int): D? = transaction {
        entityClass.findById(id)?.toDomainModel()
    }

    override fun update(id: Int, update: D): D? = transaction {
        val entity = entityClass.findById(id) ?: return@transaction null
        update.toEntity(entity)
        entity.toDomainModel()
    }

    override fun delete(id: Int): Boolean = transaction {
        val entity = entityClass.findById(id)
        entity?.delete()
        entity != null
    }

    override fun readAll(offset: Int, limit: Int, queryParams: Map<String, String>): List<D> = transaction {
        entityClass.all()
            .limit(limit, offset.toLong())
            .filter { entity ->
                queryParams.all { (key, value) ->
                    val property = entity::class.members.find { it.name == key }
                    property?.call(entity).toString().contains(value, ignoreCase = true)
                }
            }
            .map { it.toDomainModel() }
    }
}
