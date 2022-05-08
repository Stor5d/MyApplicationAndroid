package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository : PostRepository {

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
                published = "07.05.2022",
                viewCount = 99,
                likes = 99,
                shareCount = 99
            )
        }
    )

    override fun like(postId: Long) {
        data.value = posts.map { post ->
            if (post.id != postId) post
            else {
                val newLikes: Int =
                    if (post.likeByMe && post.likes > 0) post.likes - 1 else post.likes + 1
                post.copy(likeByMe = !post.likeByMe, likes = newLikes)
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