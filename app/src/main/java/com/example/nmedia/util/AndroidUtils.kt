package com.example.nmedia.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.nmedia.R
import java.text.DecimalFormat

object AndroidUtils {

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getFormattedNumber(number: Int): String =
        when {
            number >= 1000000 -> {
                DecimalFormat("#.#M").format(number.toFloat() / 1000000)
            }
            number >= 10000 -> {
                DecimalFormat("#K").format(number / 1000)
            }
            number >= 1000 -> {
                DecimalFormat("#.#K").format(number.toFloat() / 1000)
            }
            else -> {
                number.toString()
            }
        }

}