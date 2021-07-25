package com.giuseppe.pinto.blaze.bank.reader.adpaters.domain

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper

class TransactionRepositoryMySql(private val jdbcTemplate: JdbcTemplate) : TransactionRepository {


    override fun getTransactionBy(id: Long): Transaction {
        return jdbcTemplate.query("SELECT * FROM TRANSACTION WHERE ID=$id", rawMapper()).first()
    }


    private fun rawMapper(): RowMapper<Transaction> {
        return RowMapper { resultSet, _ ->
            Transaction(
                resultSet.getLong("ID"),
                TransactionType.valueOf(resultSet.getString("TYPE").uppercase()),
                resultSet.getBigDecimal("AMOUNT"),
                resultSet.getString("DESCRIPTION"),
                resultSet.getLong("USER_ID")
            )
        }
    }
}