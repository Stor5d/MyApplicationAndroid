package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val publihed: String,
    val likeByMe: Boolean = false,
    var likes: Int = 0,
    var shareCount: Int = 0,
    var viewCount: Int = 0
)