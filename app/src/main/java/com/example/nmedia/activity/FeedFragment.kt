package com.example.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.R
import com.example.nmedia.adapter.PostCallback
import com.example.nmedia.adapter.PostsAdapter
import com.example.nmedia.databinding.FragmentFeedBinding
import com.example.nmedia.dto.Post
import com.example.nmedia.viewmodel.PostViewModel


class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentFeedBinding.inflate(inflater, container, false)

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
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onVideo(post: Post) {
                if(post.videoURL.isBlank()){
                    Toast.makeText(context, "video is not found", Toast.LENGTH_SHORT).show()
                    return
                }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoURL)).apply {
                    post.videoURL
                }
                startActivity(intent)
            }

            override fun onItem(post: Post) {
                viewModel.data.value
                val bundle = Bundle().apply {
                    putLong("id",post.id)
                }
                findNavController().navigate(R.id.action_feedFragment_to_cardPostFragment,bundle)
            }

        })

        binding.mainRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val addingNewPost = adapter.itemCount < posts.size
            adapter.submitList(posts){
                if(addingNewPost){
                    binding.mainRecyclerView.scrollToPosition(0)
                }
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == 0L) {
                return@observe
            }
            val bundle = Bundle().apply {
                putString("content",post.content)
                putBoolean("edit",true)
            }
            findNavController().navigate(
                R.id.action_feedFragment_to_newPostFragment,
                bundle
            )
        }

        return binding.root
    }

}


