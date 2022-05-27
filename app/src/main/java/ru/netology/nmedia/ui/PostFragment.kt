package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
    private var postId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostFragmentBinding.inflate(layoutInflater)

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

        viewModel.playPostVideo.observe(this) { urlVideo ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo)))
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
        postId = args.postId
     viewModel.getPost(postId)?.also {post->
           bind(post, binding)
      }

        binding.postItem.likeButton.setOnClickListener { viewModel.onLikeClickedPost(postId) }

        viewModel.update.observe(viewLifecycleOwner) { postId ->
            viewModel.getPost(postId)?.also {post->
                bind(post, binding)
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
            if (post.urlVideo.isBlank()) videoGroup.visibility = android.view.ViewGroup.GONE else
                videoGroup.visibility = android.view.ViewGroup.VISIBLE
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