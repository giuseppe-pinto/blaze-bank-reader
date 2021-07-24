package com.giuseppe.pinto.blaze.bank.reader.adpaters

import com.giuseppe.pinto.blaze.bank.reader.domain.NotPresentUser
import com.giuseppe.pinto.blaze.bank.reader.domain.PresentUser
import com.giuseppe.pinto.blaze.bank.reader.domain.User
import com.giuseppe.pinto.blaze.bank.reader.domain.UserRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper

class UserRepositoryMySql(private val jdbcTemplate: JdbcTemplate) : UserRepository {

    override fun getUserInfoBy(id: Long): User {

        val users = jdbcTemplate.query("SELECT * FROM USER WHERE ID=$id", rawMapper())

        return when {
            users.isEmpty() -> NotPresentUser
            else -> users.first()
        }
    }

    private fun rawMapper(): RowMapper<User> {
        return RowMapper { resultSet, _ ->
            PresentUser(
                resultSet.getLong("ID"),
                resultSet.getString("NAME"),
                resultSet.getString("LASTNAME"),
                resultSet.getString("EMAIL"),
                resultSet.getString("PHONE"),
                resultSet.getString("ADDRESS"),
                resultSet.getString("CITY"),
                resultSet.getString("COUNTRY")
            )
        }
    }

}