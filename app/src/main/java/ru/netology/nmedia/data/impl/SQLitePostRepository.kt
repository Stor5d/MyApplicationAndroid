package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.post.Post

class SQLitePostRepository(
    private val dao:PostDao
):PostRepository {

    private val posts
        get() = checkNotNull(data.value) {
            "Не нуль"
        }

    override val data = MutableLiveData(dao.getAll())

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        data.value = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
    }

    override fun like(postId: Long) {
        dao.likeById(postId)
        data.value = posts.map {
            if (it.id != postId) it else it.copy(
                likeByMe = !it.likeByMe,
                likes = if (it.likeByMe) it.likes - 1 else it.likes + 1
            )
        }
    }

    override fun share(postId: Long) {
        TODO("Not yet implemented")
    }

    override fun delete(postId: Long) {
        dao.removeById(postId)
        data.value = posts.filter { it.id != postId }
    }




}