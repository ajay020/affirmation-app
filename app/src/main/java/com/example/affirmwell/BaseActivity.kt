package com.example.affirmwell

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.affirmwell.utils.LocaleHelper

open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleHelper.setLocale(it) })
    }
}