package com.example.telephoto.domain.models

data class ChatId (
    val chatId: Long,
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val addStatus: Boolean
)