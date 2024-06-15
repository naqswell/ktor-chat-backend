package com.naqswell.features.chat.domain.model

data class Message(
    val sessionId: String,
    val textMessage: String,
    val sender: String,
    val receiver: String,
    val timestamp: Long
)