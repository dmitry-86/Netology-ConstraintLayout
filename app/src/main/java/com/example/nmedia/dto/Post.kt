package com.example.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    //val authorAvatar: String,
    val published: String,
    val content: String,
    var likes: Int = 0,
    var shares: Int = 0,
    var likedByMe: Boolean = false
)