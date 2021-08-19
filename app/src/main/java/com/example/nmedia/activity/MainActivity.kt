package com.example.nmedia.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintHelper
import com.example.nmedia.R
import com.example.nmedia.adapter.PostCallback
import com.example.nmedia.adapter.PostsAdapter
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.dto.Post
import com.example.nmedia.util.AndroidUtils
import com.example.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.group.visibility = View.GONE

        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter(object : PostCallback {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                binding.group.visibility = View.VISIBLE

            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

        })

        binding.mainRecyclerView.adapter = adapter

        viewModel.data.observe(this) { posts ->

            adapter.submitList(posts)

        }

        viewModel.edited.observe(this){ post ->
            if(post.id == 0L){
                return@observe
            }
            binding.contentEditText.setText(post.content)
            binding.contentEditText.requestFocus()
        }

        binding.saveImageButton.setOnClickListener {
            with(binding.contentEditText){
                if(text.isNullOrBlank()){
                    Toast.makeText(this@MainActivity, R.string.error_empty_content, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.changeContext(text.toString())
                viewModel.save()
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
            binding.group.visibility = View.GONE
        }

        binding.undoEditsImageButton.setOnClickListener {
            binding.contentEditText.setText(viewModel.undoEdits())
        }

    }

}


