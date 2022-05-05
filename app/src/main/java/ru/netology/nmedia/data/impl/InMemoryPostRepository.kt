package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository() : PostRepository {

    override val data = MutableLiveData<Post>(
        Post(
            id = 0L,
            author = "Евгений",
            content = "Первый пост Первый пост Первый пост Первый пост Первый пост Первый пост Первый пост",
            publihed = "08.04.2022",
            viewCount = 1000,
            likes = 999,
            shareCount = 999
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "Не нуль"
        }
        if (currentPost.likeByMe && currentPost.likes > 0) currentPost.likes-- else currentPost.likes++
        val likePost = currentPost.copy(
            likeByMe = !currentPost.likeByMe,
            likes = currentPost.likes
        )
        data.value = likePost
    }

    override fun share() {
        val currentPost = checkNotNull(data.value) {
            "Не нуль"
        }
        currentPost.shareCount++
        val sharePost = currentPost.copy(
            shareCount = currentPost.shareCount
        )
        data.value = sharePost
    }

}