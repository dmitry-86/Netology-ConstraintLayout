package com.example.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nmedia.R
import com.example.nmedia.databinding.FragmentCardPostBinding
import com.example.nmedia.dto.Post
import com.example.nmedia.util.AndroidUtils.getFormattedNumber

interface PostCallback {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onVideo(post: Post)
    fun onItem(post: Post)
}

class PostsAdapter(private val postCallback: PostCallback) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            FragmentCardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, postCallback)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)

    }

}

class PostViewHolder(
    private val binding: FragmentCardPostBinding,
    private val postCallback: PostCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            authorTextView.text = post.author
            publishedTextView.text = post.published
            contentTextView.text = post.content

            likeButton.isChecked = post.likedByMe

            likeButton.text = getFormattedNumber(post.likes)

            shareButton.text = getFormattedNumber(post.shares)

            likeButton.setOnClickListener {
                postCallback.onLike(post)
            }

            shareButton.setOnClickListener {
                postCallback.onShare(post)
            }

            video.setOnClickListener {
                postCallback.onVideo(post)
            }

            menu.setOnClickListener {

                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_options)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.post_remove -> {
                                postCallback.onRemove(post)
                                true
                            }
                            R.id.post_edit -> {
                                postCallback.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }


            avatarImageView.setOnClickListener {
                postCallback.onItem(post)
            }

            authorTextView.setOnClickListener {
                postCallback.onItem(post)
            }

            publishedTextView.setOnClickListener {
                postCallback.onItem(post)
            }

            contentTextView.setOnClickListener {
                postCallback.onItem(post)
            }


        }

    }

}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}