package com.giuseppe.pinto.blaze.bank.reader.adpaters.domain

interface UserRepository {
    fun getUserInfoBy(id:Long) : User
}