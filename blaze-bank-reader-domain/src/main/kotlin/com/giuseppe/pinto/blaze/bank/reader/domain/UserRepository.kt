package com.giuseppe.pinto.blaze.bank.reader.domain

interface UserRepository {
    fun getUserInfoBy(id:Long) : User
}