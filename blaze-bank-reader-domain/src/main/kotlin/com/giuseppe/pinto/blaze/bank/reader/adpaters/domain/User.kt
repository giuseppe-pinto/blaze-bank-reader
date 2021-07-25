package com.giuseppe.pinto.blaze.bank.reader.adpaters.domain

sealed class User

data class PresentUser(
    val id: Long,
    val name: String,
    val lastname: String,
    val email: String,
    val phone: String,
    val address: String,
    val city: String,
    val country: String
) : User()

object NotPresentUser : User()
