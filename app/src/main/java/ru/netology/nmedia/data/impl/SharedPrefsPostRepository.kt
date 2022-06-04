package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.data.PostRepository
import kotlin.properties.Delegates

class SharedPrefsPostRepository(
    application: Application
) : PostRepository {

    private val prefs = application.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private var nextId by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ) { _, _, newValue ->
        prefs.edit { putLong(NEXT_ID_PREFS_KEY, newValue) }
    }

    private var posts
        get() = checkNotNull(data.value) {
            "Не нуль"
        }
        set(value) {
            prefs.edit {
                val serializedPosts = Json.encodeToString(value)
                putString(POSTS_PREFS_KEY, serializedPosts)
            }
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {
        val serializedPosts = prefs.getString(POSTS_PREFS_KEY, null)
        val posts: List<Post> = if (serializedPosts != null) {
            Json.decodeFromString(serializedPosts)
        } else emptyList()
        data = MutableLiveData(posts)
    }

    override fun liked(postId: Long) {
        posts = posts.map { post ->
            if (post.id != postId) post
            else {
                val newLikes: Int =
                    if (post.likeByMe && post.likes > 0) post.likes - 1 else post.likes + 1
                post.copy(likeByMe = !post.likeByMe, likes = newLikes)
            }
        }

    }

    override fun share(postId: Long) {
        posts = posts.map { post ->
            if (post.id != postId) post
            else {
                post.copy(shareCount = post.shareCount + 1)
            }
        }
    }

    override fun delete(postId: Long) {
        posts = posts.filterNot { post -> post.id == postId }
    }

    override fun insert(post: Post) {
        posts = listOf(post.copy(id = ++nextId)) + posts
    }

    override fun update(post: Post) {
        posts = posts.map {
            if (post.id == it.id) post else it
        }
    }

    companion object {
        const val GENERATED_POST_AMOUNT = 1000
        const val POSTS_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "nextId"
    }
}