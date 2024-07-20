package com.example.affirmwell.utils

import com.example.affirmwell.R
import com.example.affirmwell.model.Category
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.res.ResourcesCompat

object Utils {
    var images = listOf(
        R.drawable.grad1,
        R.drawable.grad2,
        R.drawable.grad3,
        R.drawable.grad4,
        R.drawable.grad5,
        R.drawable.img1,
    )

    var catagories = listOf(
        Category(R.drawable.self_care, name = "SelfCare"),
        Category(R.drawable.my, name = "My Affirmations"),
        Category(R.drawable.health, name = "Health"),
        Category(R.drawable.heartbroken, name = "Heartbreak"),
        Category(R.drawable.positive_thinking, name = "Positive Thinking"),
        Category(R.drawable.anxiety, name = "Anxiety"),
        Category(R.drawable.depression, name = "Depression"),
        Category(R.drawable.lonliness, name = "Loneliness"),
        Category(R.drawable.relationship, name = "Relationship"),
        Category(R.drawable.anger, name = "Anger"),
    )

    fun shareTextOverImage(context: Context, text: String, drawableResId: Int) {
        val resources = context.resources
        val drawable = ResourcesCompat.getDrawable(resources, drawableResId, null) as BitmapDrawable
        val bitmap = drawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 60f
            isAntiAlias = true
        }

        // Calculate the position to center the text
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val x = (canvas.width - bounds.width()) / 2f
        val y = (canvas.height + bounds.height()) / 2f

        canvas.drawText(text, x, y, paint)

        // Save the image to a file
        val file = File(context.cacheDir, "images")
        file.mkdirs()
        val imageFile = File(file, "shared_image.png")
        val fileOutputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()

        // Get the content URI for the file
        val contentUri: Uri =
            FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)

        // Create the share intent
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }


    fun sliderValueToTime(value: Float): String {
        val hour = value.toInt()
        val minute = ((value - hour) * 60).toInt()
        return String.format("%02d:%02d", hour, minute)
    }
}