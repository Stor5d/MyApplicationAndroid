package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel
import java.text.DecimalFormat
import kotlin.math.floor

class PostFragment : Fragment() {

    private val viewModel by activityViewModels<PostViewModel>()
    private val args by navArgs<PostFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

        viewModel.navigateToPostContentScreenEvent.observe(this) { initialContent ->
            val direction =
                PostFragmentDirections.actionPostFragmentToPostContentFragment(initialContent)
            findNavController().navigate(direction)
        }

        viewModel.playPostVideo.observe(this) { urlVideo ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo)))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->
        val postId = args.postId
        viewModel.setCurrentPost(postId)

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            posts.firstOrNull { post ->
                post.id == postId
            }?.let { bind(it, binding) }

            val popupMenu by lazy {
                PopupMenu(context, binding.postItem.menu).apply {
                    inflate(R.menu.option_post)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                viewModel.onRemoveClickedPost()
                                val direction =
                                    PostFragmentDirections.actionPostFragmentToFeedFragment()
                                findNavController().navigate(direction)
                                true
                            }
                            R.id.edit -> {
                                viewModel.onEditClickedPost()
                                true
                            }
                            else -> false
                        }
                    }
                }
            }

            with(binding.postItem) {
                likeButton.setOnClickListener { viewModel.onLikeClickedPost() }
                shareButton.setOnClickListener { viewModel.onShareClickedPost() }
                menu.setOnClickListener { popupMenu.show() }
                play.setOnClickListener { viewModel.onPlayClickedPost() }

            }
        }


    }.root

    private fun bind(post: Post, binding: PostFragmentBinding) {
        with(binding.postItem) {
            authorName.text = post.author
            contentEditText.text = post.content
            date.text = post.published
            shareButton.text = likesShareViewToString(post.shareCount)
            viewButton.text = likesShareViewToString(post.viewCount)
            likeButton.text = likesShareViewToString(post.likes)
            likeButton.isChecked = post.likeByMe
            if (post.urlVideo.isBlank()) videoGroup.visibility = ViewGroup.GONE else
                videoGroup.visibility = ViewGroup.VISIBLE
        }
    }

    private fun likesShareViewToString(likesOrShare: Int): String {
        val format = DecimalFormat("#.#")
        return when {
            likesOrShare < 1_000 -> "$likesOrShare"
            likesOrShare in 1_000..9_999 -> {
                val text = format.format((floor(likesOrShare / 100.0)) / 10)
                getString(R.string.thousand, text)

            }
            likesOrShare in 10_000..999_999 -> {
                val text = (likesOrShare / 1_000).toString()
                getString(R.string.thousand, text)
            }
            likesOrShare >= 1_000_000 -> {
                val text = format.format((floor(likesOrShare / 100_000.0)) / 10)
                getString(R.string.million, text)
            }
            else -> "-"
        }
    }

}