package com.example.note_taking_app.utils

import android.app.Activity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat

fun Activity.setStatusBarColorCompat(colorResId: Int) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = ContextCompat.getColor(this, colorResId)
}