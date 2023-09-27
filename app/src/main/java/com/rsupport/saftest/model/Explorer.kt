package com.rsupport.saftest.model

data class Explorer(
    val clientId: String,
    val type: Int,
    val category: Int,
    val messageId: Int,
    val messageKey: Int,
    val message: MessageItem
)
