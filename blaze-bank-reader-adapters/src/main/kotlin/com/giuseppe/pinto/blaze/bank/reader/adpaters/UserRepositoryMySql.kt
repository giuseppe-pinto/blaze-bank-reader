package com.giuseppe.pinto.blaze.bank.reader.adpaters

import com.giuseppe.pinto.blaze.bank.reader.domain.User
import com.giuseppe.pinto.blaze.bank.reader.domain.UserRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper

class UserRepositoryMySql(private val jdbcTemplate: JdbcTemplate) : UserRepository {

    override fun getUserInfo(id: Long): User {
        return jdbcTemplate.query("SELECT * FROM USER WHERE ID=$id", rawMapper()).first()
    }

    private fun rawMapper(): RowMapper<User> {
        TODO("Not yet implemented")
    }

}