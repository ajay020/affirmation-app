package com.example.affirmwell.utils

import com.example.affirmwell.R
import com.example.affirmwell.model.Category

object Utils {
    var images = listOf(
        R.drawable.grad1,
        R.drawable.grad2,
        R.drawable.grad3,
        R.drawable.grad4,
        R.drawable.grad5,
        R.drawable.grad6,
        R.drawable.grad7,
        R.drawable.img1,
    )

    var catagories = listOf(
        Category(R.drawable.grad1, name = "General"),
        Category(R.drawable.grad2, name = "Health"),
        Category(R.drawable.grad3, name = "Anxiety"),
        Category(R.drawable.grad4, name = "Anger"),
        Category(R.drawable.grad5, name = "Depression"),
        Category(R.drawable.grad6, name = "Loneliness"),
        Category(R.drawable.grad6, name = "Relationship"),

//        Category(R.drawable.grad7, name = "Stress"),
//        Category(R.drawable.img1, name = "Love"),
//        Category(R.drawable.grad2, name = "Family"),
//        Category(R.drawable.grad3, name = "Career"),
//        Category(R.drawable.grad4, name = "Self-care"),
//        Category(R.drawable.grad5, name = "Self-Esteem"),
    )


    fun sliderValueToTime(value: Float): String {
        val hour = value.toInt()
        val minute = ((value - hour) * 60).toInt()
        return String.format("%02d:%02d", hour, minute)
    }

}