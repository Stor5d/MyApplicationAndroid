package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.post.Post

interface PostRepository {

    val data:LiveData<List<Post>>

    fun liked(postId:Long)
    fun share(postId:Long)
    fun delete(postId:Long)
    fun update(post: Post)
    fun insert(post: Post)

    companion object{
        const val NEW_POST_ID = 0L
    }
}