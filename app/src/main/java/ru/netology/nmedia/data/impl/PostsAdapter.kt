package ru.netology.nmedia.data.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import java.text.DecimalFormat
import kotlin.math.floor

internal class PostsAdapter(
    private val onLikeClicked: (Post) -> Unit,
    private val onShareClicked: (Post) -> Unit
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: PostListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.likeButton.setOnClickListener { onLikeClicked(post) }
            binding.shareButton.setOnClickListener { onShareClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                content.text = post.content
                date.text = post.publihed
                likesTextCount.text = likesShareViewToString(post.likes)
                sharesTextCount.text = likesShareViewToString(post.shareCount)
                viewsTextCount.text = likesShareViewToString(post.viewCount)
                likeButton.setImageResource(getLikeIconResId(post.likeByMe))
            }
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
                    itemView.context.getString(R.string.thousand, text)

                }
                likesOrShare in 10_000..999_999 -> {
                    val text = (likesOrShare / 1_000).toString()
                    itemView.context.getString(R.string.thousand, text)
                }
                likesOrShare >= 1_000_000 -> {
                    val text = format.format((floor(likesOrShare / 100_000.0)) / 10)
                    itemView.context.getString(R.string.million, text)
                }
                else -> "-"
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem

    }

}