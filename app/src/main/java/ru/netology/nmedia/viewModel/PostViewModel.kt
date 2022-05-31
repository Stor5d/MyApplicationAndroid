package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.AppPostRepository
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(application: Application) : AndroidViewModel(application),
    PostInteractionListener {

    private val repository: PostRepository = AppPostRepository.getInstance(application)

    val data by repository::data

    private val currentPost = MutableLiveData<Post?>(null)
    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()
    val playPostVideo = SingleLiveEvent<String>()
    val navigateToPostEvent = SingleLiveEvent<Long>()

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return
        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "Today"
        )
        repository.save(post)
        currentPost.value = null
    }

    override fun onLikeClicked(post: Post) = repository.like(post.id)
    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        sharePostContent.value = post.content
    }

    override fun onRemoveClicked(post: Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.value = post.content
    }

    override fun onPlayClicked(post: Post) {
        playPostVideo.value = post.urlVideo
    }

    override fun onToPost(post: Post) {
        currentPost.value = post
        navigateToPostEvent.value = post.id
    }

    fun onAddClicked() {
        currentPost.value = null
        navigateToPostContentScreenEvent.call()
    }

    fun setCurrentPost(postId: Long) {
        currentPost.value = data.value?.firstOrNull { post ->
            post.id == postId
        }
    }

    fun onLikeClickedPost() {
        currentPost.value?.let { repository.like(it.id) }
    }

    fun onShareClickedPost() {
        currentPost.value?.let { repository.share(it.id) }
        sharePostContent.value = currentPost.value?.content
    }

    fun onRemoveClickedPost() {
        currentPost.value?.let { repository.delete(it.id) }
    }

    fun onEditClickedPost() {
        navigateToPostContentScreenEvent.value = currentPost.value?.content
    }

    fun onPlayClickedPost() {
        playPostVideo.value = currentPost.value?.urlVideo
    }

}
