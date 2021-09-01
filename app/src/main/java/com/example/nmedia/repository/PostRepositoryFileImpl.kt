package com.example.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nmedia.dto.Post
import com.example.nmedia.util.AndroidUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class PostRepositoryFileImpl(private val context: Context): PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    private var nextId = 1L
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    private val month = AndroidUtils.fromNumToMonth(SimpleDateFormat("MM").format(Date()))
    private val timeStamp: String = SimpleDateFormat("dd $month в HH:mm").format(Date())


    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            try {
                context.openFileInput(filename).bufferedReader().use {
                    posts = gson.fromJson(it, type)
                    data.value = posts
                }
            } catch (e: Exception) {
                sync()
            }
        } else {
            sync()
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
        sync()

    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = timeStamp
                )
            ) + posts
            data.value = posts
            sync()
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
}