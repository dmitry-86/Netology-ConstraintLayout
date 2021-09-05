package com.example.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.R
import com.example.nmedia.databinding.FragmentCardPostBinding
import com.example.nmedia.util.AndroidUtils
import com.example.nmedia.viewmodel.PostViewModel


class CardPostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentCardPostBinding.inflate(inflater, container, false)

        val id = arguments?.getLong("id")

        if (id == null) {
            findNavController().navigate(R.id.feedFragment)
            return binding.root
        }

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            posts.map { post ->
                if (post.id == id) {
                    binding.apply {

                        authorTextView.text = post.author
                        avatarImageView
                        publishedTextView.text = post.published
                        contentTextView.text = post.content
                        likeButton.text = AndroidUtils.getFormattedNumber(post.likes)
                        shareButton.text = AndroidUtils.getFormattedNumber(post.shares)
                        likeButton.isChecked = post.likedByMe

                        likeButton.setOnClickListener {
                            viewModel.likeById(id)
                        }

                        shareButton.setOnClickListener {
                            viewModel.shareById(id)
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, post.content)
                                type = "text/plain"
                            }

                            val shareIntent =
                                Intent.createChooser(intent, getString(R.string.chooser_share_post))
                            startActivity(shareIntent)
                        }

                        video.setOnClickListener {
                            if (post.videoURL.isBlank()) {
                                Toast.makeText(context, "video is not found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(post.videoURL)).apply {
                                    post.videoURL
                                }
                            startActivity(intent)
                        }

                        menu.setOnClickListener {

                            PopupMenu(it.context, it).apply {
                                inflate(R.menu.post_options)
                                setOnMenuItemClickListener { menuItem ->
                                    when (menuItem.itemId) {
                                        R.id.post_remove -> {
                                            viewModel.removeById(id)
                                            findNavController().navigate(R.id.feedFragment)
                                            true
                                        }
                                        R.id.post_edit -> {
                                            viewModel.edit(post)
                                            val bundle = Bundle().apply {
                                                putString("content", post.content)
                                            }
                                            findNavController().navigate(
                                                R.id.newPostFragment,
                                                bundle
                                            )
                                            true
                                        }
                                        else -> false
                                    }
                                }
                            }.show()
                        }

                    }
                }
            }

        }

        return binding.root

    }

}