package com.example.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val videoURL: String = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
    var likes: Int = 0,
    var shares: Int = 0,
    var likedByMe: Boolean = false
)