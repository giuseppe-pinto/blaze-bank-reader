package com.giuseppe.pinto.blaze.bank.reader.domain

interface UserRepository {
    fun getUserInfo(id:Long) : User
}