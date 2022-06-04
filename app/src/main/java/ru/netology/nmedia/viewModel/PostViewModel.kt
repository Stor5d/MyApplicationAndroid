package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.SQLitePostRepository
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(application: Application) : AndroidViewModel(application),
    PostInteractionListener {

    //private val repository: PostRepository = AppPostRepository.getInstance(application)
    private val repository: PostRepository = SQLitePostRepository(
        dao = AppDb.getInstance(
            context = application
        ).postDao
    )

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()
    val playPostVideo = SingleLiveEvent<String>()
    val navigateToPostEvent = SingleLiveEvent<Long>()
    private var currentPostId: Long = -1

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return
        if(currentPostId==-1L) {
            repository.insert(Post(
                id = PostRepository.NEW_POST_ID,
                author = "Me",
                content = content,
                published = "Today"
            ))
        } else {
            getPost(currentPostId)?.copy(content = content)?.let { repository.update(it) }
        }
        currentPostId = -1
    }

    override fun onLikeClicked(post: Post) = repository.liked(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        sharePostContent.value = post.content
    }

    override fun onRemoveClicked(post: Post) = repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPostId = post.id
        navigateToPostContentScreenEvent.value = post.content
    }

    override fun onPlayClicked(post: Post) {
        playPostVideo.value = post.urlVideo
    }

    override fun onToPost(post: Post) {
        currentPostId = post.id
        navigateToPostEvent.value = post.id
    }

    fun onAddClicked() {
        currentPostId = -1
        navigateToPostContentScreenEvent.call()
    }

    fun onLikeClickedPost() {
        repository.liked(currentPostId)
    }

    fun onShareClickedPost() {
        repository.share(currentPostId)
        sharePostContent.value = getPost(currentPostId)?.content
    }


    fun onRemoveClickedPost() {
        repository.delete(currentPostId)
    }

    fun onEditClickedPost() {
        navigateToPostContentScreenEvent.value = getPost(currentPostId)?.content
    }

    fun onPlayClickedPost() {
        playPostVideo.value = getPost(currentPostId)?.urlVideo
    }

    private fun getPost(postId: Long) = data.value?.firstOrNull { post ->
        post.id == postId
    }

}
