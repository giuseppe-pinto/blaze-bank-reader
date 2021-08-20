package com.giuseppe.pinto.blaze.bank.reader.adpaters.domain
import java.math.BigDecimal

sealed class Transaction

data class PresentTransaction(
    val id: Long,
    val transactionType: TransactionType,
    val amount: BigDecimal,
    val description: String,
    val userId: Long
) : Transaction()

object NotPresentTransaction : Transaction()

