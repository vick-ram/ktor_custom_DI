package tech.vickram.models

import tech.vickram.db.UserEntity
import tech.vickram.repository.CrudRepositoryImpl


object DI {
    val userRepository by lazy { UserRepository() }
}

class UserRepository : CrudRepositoryImpl<UserEntity, User>(UserEntity) {
    override fun UserEntity.toDomainModel(): User = toDomain()
    override fun User.toEntity(entity: UserEntity) {
        entity.username = username
        entity.email = email
        entity.password = password
    }
}