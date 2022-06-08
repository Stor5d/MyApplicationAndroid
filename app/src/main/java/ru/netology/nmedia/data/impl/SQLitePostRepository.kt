package ru.netology.nmedia.data.impl

import androidx.lifecycle.map
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel
import ru.netology.nmedia.post.Post

class SQLitePostRepository(
    private val dao: PostDao
) : PostRepository {

    override val data = dao.getAll().map {entities->
        entities.map { it.toModel() }
    }

    override fun liked(postId: Long) {
        dao.likeById(postId)
    }

    override fun share(postId: Long) {
        dao.share(postId)
    }

    override fun delete(postId: Long) {
        dao.removeById(postId)
    }

    override fun update(post: Post) {
        dao.updateContentById(post.id, post.content)
    }

    override fun insert(post: Post) {
        dao.insert(post.toEntity())
    }


}