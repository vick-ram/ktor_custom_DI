package tech.vickram.repository

interface CrudRepository<T, ID> {
    fun create(entity: T): T
    fun read(id: ID): T?
    fun update(id: ID, update: T): T?
    fun delete(id: ID): Boolean
    fun readAll(offset: Int, limit: Int, queryParams: Map<String, String>): List<T>
}
