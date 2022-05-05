package com.example.telephoto.data.storage.client.models

data class ClientSQLite (
    val chatId: Long? = 0L,
    val firstName: String? = "",
    val lastName: String? = "",
    val nickname: String? = "",
    val addStatus: Boolean? = false
)