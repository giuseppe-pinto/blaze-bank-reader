package com.giuseppe.pinto.blaze.bank.reader.adpaters

interface UserRepositoryDao {

    fun getUser(id:Long) : UserDto

}
