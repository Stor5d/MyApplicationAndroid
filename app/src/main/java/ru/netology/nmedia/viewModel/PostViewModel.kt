package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.post.PostListener
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(application: Application) : AndroidViewModel(application),
    PostInteractionListener, PostListener {

    private val repository: PostRepository = FilePostRepository(application)

    val data by repository::data

    private val currentPost = MutableLiveData<Post?>(null)
    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()
    val playPostVideo = SingleLiveEvent<String>()
    val navigateToPostEvent = SingleLiveEvent<Long>()
    val update = MutableLiveData<Long>(0)

    fun getPost(postId:Long):Post? {
        return repository.data.value?.firstOrNull { post ->
            postId == post.id
        }
    }

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

    // region PostInteractionListener

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

    override fun onToPost(postId: Long) {
        navigateToPostEvent.value = postId
    }

    // endregion PostInteractionListener

    fun onAddClicked() {
        navigateToPostContentScreenEvent.call()
    }

    // region PostListener

    override fun onLikeClickedPost(postId: Long) {
        repository.like(postId)
        update.value = postId
    }

    override fun onShareClicked(postId: Long) {
        TODO("Not yet implemented")
    }

    override fun onRemoveClicked(postId: Long) {
        TODO("Not yet implemented")
    }

    override fun onEditClicked(postId: Long) {
        TODO("Not yet implemented")
    }

    override fun onPlayClicked(postId: Long) {
        TODO("Not yet implemented")
    }

    // region PostListener

}
