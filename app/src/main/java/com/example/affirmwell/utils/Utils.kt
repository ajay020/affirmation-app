package com.example.affirmwell.utils

import com.example.affirmwell.R

object Utils {
    var images = listOf(
        R.drawable.img1,
        R.drawable.ic_launcher_foreground,
        R.drawable.img1,
        R.drawable.img1,
        R.drawable.img1,
        R.drawable.img1,
        R.drawable.img1,
        R.drawable.img1,
    )

    fun sliderValueToTime(value: Float): String {
        val hour = value.toInt()
        val minute = ((value - hour) * 60).toInt()
        return String.format("%02d:%02d", hour, minute)
    }

}