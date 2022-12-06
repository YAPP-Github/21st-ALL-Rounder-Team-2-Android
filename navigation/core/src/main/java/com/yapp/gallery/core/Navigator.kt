package com.yapp.gallery.core

import android.content.Context
import android.content.Intent

interface Navigator {
    fun navigate(context: Context) : Intent
}
