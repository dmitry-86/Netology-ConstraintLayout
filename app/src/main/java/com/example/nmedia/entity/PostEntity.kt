package com.example.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nmedia.dto.Post
import com.example.nmedia.util.AndroidUtils

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val videoURL: String = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
    var likes: Int = 0,
    var shares: Int = 0,
    var likedByMe: Boolean = false
){
    fun toDto() = Post(
        id,
        author,
        published,
        content,
        videoURL,
        likes,
        shares,
        likedByMe)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.published, dto.content, dto.videoURL, dto.likes, dto.shares, dto.likedByMe)
    }
}