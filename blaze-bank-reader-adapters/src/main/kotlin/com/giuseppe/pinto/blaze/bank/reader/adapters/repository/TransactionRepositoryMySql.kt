package com.giuseppe.pinto.blaze.bank.reader.adapters.repository

import com.giuseppe.pinto.blaze.bank.reader.domain.model.NotPresentTransaction
import com.giuseppe.pinto.blaze.bank.reader.domain.model.PresentTransaction
import com.giuseppe.pinto.blaze.bank.reader.domain.model.Transaction
import com.giuseppe.pinto.blaze.bank.reader.domain.model.TransactionType
import com.giuseppe.pinto.blaze.bank.reader.domain.repository.TransactionRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper

class TransactionRepositoryMySql(private val jdbcTemplate: JdbcTemplate) : TransactionRepository {


    override fun getTransactionBy(transactionId: Long): Transaction {
        val transactions = jdbcTemplate.query("SELECT * FROM TRANSACTION WHERE ID=$transactionId", rawMapper())

        return when {
            transactions.isEmpty() -> NotPresentTransaction
            else ->  transactions.first()
        }
    }

    override fun getTransactionsOf(userId: Long): List<Transaction> {
        val transactions = jdbcTemplate.query("SELECT * FROM TRANSACTION WHERE USER_ID=$userId", rawMapper())

        return when {
            transactions.isEmpty() -> listOf<NotPresentTransaction>()
            else ->  transactions
        }
    }

    private fun rawMapper(): RowMapper<Transaction> {
        return RowMapper { resultSet, _ ->
            PresentTransaction(
                resultSet.getLong("ID"),
                TransactionType.valueOf(resultSet.getString("TYPE").uppercase()),
                resultSet.getBigDecimal("AMOUNT"),
                resultSet.getString("DESCRIPTION"),
                resultSet.getLong("USER_ID")
            )
        }
    }
}