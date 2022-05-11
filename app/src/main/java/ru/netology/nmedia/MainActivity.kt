package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)
        binding.postRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)
                clearFocus()
                hideKeyboard()
            }
        }

        binding.editFragment.closeButton.setOnClickListener {
            with(binding) {
                group.visibility = View.GONE
                editFragment.authorName.text = null
                editFragment.content.text = null
                contentEditText.text = null
                contentEditText.clearFocus()
                contentEditText.hideKeyboard()
            }
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(binding) {
                contentEditText.setText(currentPost?.content)
                if (currentPost != null) {
                    group.visibility = View.VISIBLE
                    editFragment.authorName.text = currentPost.author
                    editFragment.content.text = currentPost.content
                } else group.visibility = View.GONE
            }

        }

    }
}