package com.rsupport.saftest.model

data class MessageItem(
    val path: String,
    val state: Int,
    val error: Int,
    val isContinue: Int,
    val directoryInfo: ExplorerItem,
    val currentInfo: ExplorerItem
)
