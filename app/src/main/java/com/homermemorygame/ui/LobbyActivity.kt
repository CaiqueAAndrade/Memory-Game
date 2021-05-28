package com.homermemorygame.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.homermemorygame.R
import com.homermemorygame.databinding.ActivityLobbyBinding


class LobbyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLobbyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lobby)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val wic = WindowInsetsControllerCompat(window, window.decorView)
            wic.isAppearanceLightStatusBars = true
            window.statusBarColor = Color.WHITE
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}