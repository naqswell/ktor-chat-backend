package com.naqswell.features.chat.domain.model

data class FriendInfo(
    val username: String? = null,
    val email: String? = null,
    val avatar: String? = null,
    val lastMessage: Message? = null
)