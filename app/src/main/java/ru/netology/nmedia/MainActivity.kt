package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.viewModel.PostViewModel
import java.text.DecimalFormat
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = PostListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            binding.render(post)
        }

        binding.likeButton.setOnClickListener {
            viewModel.onLikeClicked()
        }

        binding.shareButton.setOnClickListener {
            viewModel.onShareClicked()
        }

    }

    private fun PostListItemBinding.render(post: Post) {
        authorName.text = post.author
        content.text = post.content
        date.text = post.publihed
        likesTextCount.text = likesShareViewToString(post.likes)
        sharesTextCount.text = likesShareViewToString(post.shareCount)
        viewsTextCount.text = likesShareViewToString(post.viewCount)
        likeButton.setImageResource(getLikeIconResId(post.likeByMe))
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_liked_24dp else R.drawable.ic_baseline_favorite_border_24dp

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