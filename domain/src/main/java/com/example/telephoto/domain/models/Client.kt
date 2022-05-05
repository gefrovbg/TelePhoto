package com.example.telephoto.domain.models

data class Client (
    val chatId: Long? = 0L,
    val firstName: String? = "",
    val lastName: String? = "",
    val nickname: String? = "",
    val addStatus: Boolean? = false
)