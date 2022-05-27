package ru.netology.nmedia.post

import ru.netology.nmedia.post.Post

interface PostListener {

    fun onLikeClickedPost(postId:Long)
    fun onShareClicked(postId:Long)
    fun onRemoveClicked(postId:Long)
    fun onEditClicked(postId:Long)
    fun onPlayClicked(postId:Long)
}