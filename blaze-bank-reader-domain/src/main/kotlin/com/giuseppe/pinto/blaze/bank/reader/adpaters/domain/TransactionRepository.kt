package com.giuseppe.pinto.blaze.bank.reader.adpaters.domain

interface TransactionRepository {

    fun getTransactionBy(id : Long) : Transaction

}