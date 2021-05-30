package com.homermemorygame.extensions

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Long.millisToMinutes() : String {
    return SimpleDateFormat("mm:ss:SS").format(Date(this))
}