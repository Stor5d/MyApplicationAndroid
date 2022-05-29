package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.AppPostRepository
import ru.netology.nmedia.post.PostListener
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(application: Application) : AndroidViewModel(application),
    PostListener {

    private val repository: PostRepository = AppPostRepository.getInstance(application)

    val data by repository::data

    private val currentPost = MutableLiveData<Post?>(null)
    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()
    val playPostVideo = SingleLiveEvent<String>()

    fun setCurrentPost(postId: Long) {
        currentPost.value = data.value?.firstOrNull { post ->
            post.id == postId
        }
    }

    override fun onLikeClickedPost() {
        currentPost.value?.let { repository.like(it.id) }
    }

    override fun onShareClickedPost() {
        currentPost.value?.let { repository.share(it.id) }
        sharePostContent.value = currentPost.value?.content
    }

    override fun onRemoveClickedPost() {
        currentPost.value?.let { repository.delete(it.id) }
    }

    override fun onEditClickedPost() {
        navigateToPostContentScreenEvent.value = currentPost.value?.content
    }

    override fun onPlayClickedPost() {
        playPostVideo.value = currentPost.value?.urlVideo
    }

}
