package com.giuseppe.pinto.blaze.bank.reader.adpaters

import com.giuseppe.pinto.blaze.bank.reader.domain.User
import com.giuseppe.pinto.blaze.bank.reader.domain.UserRepository

class UserRepositoryMySql(val dao: UserRepositoryDao) : UserRepository {

    override fun getUserInfo(id: Long): User {
        return convert(dao.getUser(id))
    }

    private fun convert(user: UserDto): User {
        return User(
            user.id,
            user.name,
            user.lastname,
            user.email,
            user.phone,
            user.address,
            user.city,
            user.country
        )
    }
}