package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_POST_AMOUNT.toLong()

    private val posts
        get() = checkNotNull(data.value) {
            "Не нуль"
        }

    override val data = MutableLiveData(
        List(GENERATED_POST_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = "Netology",
                content = "Текст поста $index",
                published = "07.05.2022",
                viewCount = 999,
                likes = 999,
                shareCount = 100,
                urlVideo = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
            )
        }
    )

    override fun liked(postId: Long) {
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

    override fun delete(postId: Long) {
        data.value = posts.filterNot { post -> post.id == postId }
    }

    override fun insert(post: Post) {
        data.value = listOf(post.copy(id = ++nextId)) + posts
    }

    override  fun update(post: Post) {
        data.value = posts.map {
            if (post.id == it.id) post else it
        }
    }

    companion object {
        const val GENERATED_POST_AMOUNT = 1000
    }
}