package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likeByMe: Boolean = false,
    val likes: Int = 0,
    val shareCount: Int = 0,
    val viewCount: Int = 0
)