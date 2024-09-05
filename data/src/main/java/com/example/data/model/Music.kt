package com.example.data.model

data class Music(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val filePath: String,
    val coverImage: ByteArray?
)
