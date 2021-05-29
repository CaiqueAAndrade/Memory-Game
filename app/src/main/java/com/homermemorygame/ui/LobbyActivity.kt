package com.homermemorygame.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.homermemorygame.R
import com.homermemorygame.databinding.ActivityLobbyBinding
import com.homermemorygame.model.GameMode


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

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.apply {
            btThreeByFour.setOnClickListener {
                MemoryGameActivity.start(this@LobbyActivity, GameMode.THREE_BY_FOUR)
            }
            btFiveByTwo.setOnClickListener {
                MemoryGameActivity.start(this@LobbyActivity, GameMode.FIVE_BY_TWO)
            }
            btFourByFour.setOnClickListener {
                MemoryGameActivity.start(this@LobbyActivity, GameMode.FOUR_BY_FOUR)
            }
            btFourByFive.setOnClickListener {
                MemoryGameActivity.start(this@LobbyActivity, GameMode.FOUR_BY_FIVE)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}