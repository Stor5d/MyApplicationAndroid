package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository() : PostRepository {

    private val posts
        get() = checkNotNull(data.value) {
            "Не нуль"
        }

    override val data = MutableLiveData(
        List(10) { index ->
            Post(
                id = index + 1L,
                author = "Netology",
                content = "Текст поста $index",
                publihed = "07.05.2022",
                viewCount = 999,
                likes = 999,
                shareCount = 999
            )
        }
    )

    override fun like(postId: Long) {
        data.value = posts.map { post ->
            if (post.id != postId) post
            else {
                if (post.likeByMe && post.likes > 0) post.likes-- else post.likes++
                post.copy(likeByMe = !post.likeByMe, likes = post.likes)
            }
        }

    }

    override fun share(postId: Long) {
        data.value = posts.map { post ->
            if (post.id != postId) post
            else {
                post.copy(shareCount = post.shareCount + 1)
            }
        }
    }
}