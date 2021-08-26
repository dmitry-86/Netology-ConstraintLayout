package com.example.nmedia.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.nmedia.R
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.databinding.ActivityNewPostBinding
import com.example.nmedia.util.AndroidUtils
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editEditText.setText(intent.getStringExtra(Intent.EXTRA_TEXT))
        binding.editEditText.requestFocus()
        binding.group.visibility = View.VISIBLE

        binding.undoEditsImageButton.setOnClickListener {
            binding.editEditText.setText("")
            AndroidUtils.hideKeyboard(binding.editEditText)
        }

        binding.okFloatingButton.setOnClickListener{
            val intent = Intent()
            if(binding.editEditText.text.isNullOrBlank()){
                setResult(Activity.RESULT_CANCELED, intent)
                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_SHORT).show()
            } else {
                val content = binding.editEditText.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }

    }
}