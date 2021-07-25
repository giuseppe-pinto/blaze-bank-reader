package com.giuseppe.pinto.blaze.bank.reader.adpaters.domain

import java.math.BigDecimal

data class Transaction(
    val id: Long,
    val transactionType: TransactionType,
    val amount: BigDecimal,
    val description: String,
    val userId: Long
)
