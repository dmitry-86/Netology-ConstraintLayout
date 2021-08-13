package com.example.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.activity.viewModels
import com.example.nmedia.R
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.dto.Post
import com.example.nmedia.viewmodel.PostViewModel
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        viewModel.data.observe(this) { post ->


            with(binding) {

                authorTextView.text = post.author
                publishedTextView.text = post.published
                contentTextView.text = post.content

                //лайки
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_liked_24 else R.drawable.ic_baseline_like_border_24
                )
                likesTextView.text = getFormattedNumber(post.likes)

                //репосты
                sharesTextView.text = getFormattedNumber(post.shares)

            }

            binding.like.setOnClickListener {
                viewModel.like()
            }

            binding.share.setOnClickListener {
                viewModel.share()
            }

        }
    }

    private fun getFormattedNumber(number: Int): String =
        when {
            number >= 1000000 -> {
                DecimalFormat("#.#M").format((number.toFloat() / 1000000))
            }
            number >= 10000 -> {
                DecimalFormat("#K").format((number / 1000))
            }
            number >= 1000 -> {
                DecimalFormat("#.#K").format((number.toFloat() / 1000))
            }
            else -> {
                number.toString()
            }
        }

}


