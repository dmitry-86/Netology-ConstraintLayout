package com.example.nmedia.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
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

        //binding.group.visibility = View.GONE

        val viewModel: PostViewModel by viewModels()

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContext(result)
            viewModel.save()
        }

        val adapter = PostsAdapter(object : PostCallback {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)

                newPostLauncher.launch(post.content)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onVideo(post: Post) {
                if(post.videoURL.isBlank()){
                    Toast.makeText(this@MainActivity, "video is not found", Toast.LENGTH_SHORT).show()
                    return
                }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoURL)).apply {
                    post.videoURL
                }
                startActivity(intent)
            }

        })


        binding.mainRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }


        binding.fab.setOnClickListener {
            newPostLauncher.launch("")
        }


//        viewModel.edited.observe(this) { post ->
//            if (post.id == 0L) {
//                return@observe
//            }
//            binding.contentEditText.setText(post.content)
//            binding.contentEditText.requestFocus()
//        }


//        binding.saveImageButton.setOnClickListener {
//            with(binding.contentEditText){
//                if(text.isNullOrBlank()){
//                    Toast.makeText(this@MainActivity, R.string.error_empty_content, Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//                viewModel.changeContext(text.toString())
//                viewModel.save()
//                setText("")
//                clearFocus()
//                AndroidUtils.hideKeyboard(this)
//            }
//            binding.group.visibility = View.GONE
//        }
//
//        binding.undoEditsImageButton.setOnClickListener {
//            with(binding.contentEditText) {
//                setText("")
//                AndroidUtils.hideKeyboard(this)
//            }
//            binding.group.visibility = View.GONE
//        }

    }

}


