package ru.netology.nmedia.post

import ru.netology.nmedia.post.Post

interface PostListener {

    fun onLikeClickedPost()
    fun onShareClickedPost()
    fun onRemoveClickedPost()
    fun onEditClickedPost()
    fun onPlayClickedPost()
}