package com.example.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.dto.Post
import java.text.DecimalFormat
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            authorAvatar  = "@sample/posts_avatars",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likedByMe = false
        )

        with(binding){

            authorTextView.text = post.author
            publishedTextView.text = post.published
            contentTextView.text = post.content
            likesTextView.text = getFormattedNumber(post.likes) //post.likes.toString()
            sharesTextView.text = getFormattedNumber(post.shares)//post.shares.toString()

            if(post.likedByMe){
                like?.setImageResource(R.drawable.ic_baseline_liked_24)
            }

            root.setOnClickListener {
                Log.d("check", "root")
            }

            avatar.setOnClickListener{
                Log.d("check", "avatar")
            }

            //лайки
            like?.setOnClickListener{
                Log.d("check", "like")
                post.likedByMe = !post.likedByMe
                like.setImageResource(if(post.likedByMe) R.drawable.ic_baseline_liked_24 else R.drawable.ic_baseline_like_border_24)

                if(post.likedByMe) post.likes = post.likes + 1 else post.likes = post.likes - 1
                val numOfLikes = getFormattedNumber(post.likes)
                likesTextView.setText(numOfLikes)

            }

            //репосты
            share?.setOnClickListener{
                post.shares = post.shares + 1
                val numOfShares = getFormattedNumber(post.shares)
                sharesTextView.setText(numOfShares)
            }


        }


}
    private fun getFormattedNumber(number: Int): String =
        when {
            number >= 1000000 -> {
                DecimalFormat("#.#M").format((number.toFloat() / 1000000))
            }
            number>=10000 -> {
                DecimalFormat("#K").format((number/ 1000))
            }
            number>=1000 -> {
                DecimalFormat("#.#K").format((number.toFloat()/ 1000))
            }
            else -> {
                number.toString()
            }
        }

}


