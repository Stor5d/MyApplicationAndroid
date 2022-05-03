package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val publihed: String,
    var likes: Int = 0,
    var likeByMe: Boolean = false
)