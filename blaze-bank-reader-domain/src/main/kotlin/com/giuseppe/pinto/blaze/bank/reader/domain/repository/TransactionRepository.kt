package com.giuseppe.pinto.blaze.bank.reader.domain.repository

import com.giuseppe.pinto.blaze.bank.reader.domain.model.Transaction

interface TransactionRepository {

    fun getTransactionBy(id : Long) : Transaction

}