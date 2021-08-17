package com.example.nmedia.utils

import java.text.DecimalFormat

object PostUtils {

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