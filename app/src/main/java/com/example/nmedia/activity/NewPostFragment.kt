package com.example.nmedia.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.nmedia.R
import com.example.nmedia.databinding.FragmentNewPostBinding
import com.example.nmedia.util.AndroidUtils
import com.example.nmedia.util.StringArg
import com.example.nmedia.viewmodel.PostViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        binding.editEditText.setText(arguments?.getString("content"))
        binding.editEditText.requestFocus()

        binding.okFloatingButton.setOnClickListener {
            viewModel.changeContent(binding.editEditText.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            //findNavController().popBackStack()
        }
        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        return binding.root
    }
}