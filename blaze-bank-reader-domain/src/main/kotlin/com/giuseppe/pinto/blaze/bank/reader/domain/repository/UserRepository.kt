package com.giuseppe.pinto.blaze.bank.reader.domain.repository

import com.giuseppe.pinto.blaze.bank.reader.domain.model.User

interface UserRepository {
    fun getUserInfoBy(id:Long) : User
}