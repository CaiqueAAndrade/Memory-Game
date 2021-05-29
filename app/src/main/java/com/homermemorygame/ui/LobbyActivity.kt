package com.homermemorygame.ui

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.homermemorygame.R
import com.homermemorygame.databinding.ActivityLobbyBinding
import com.homermemorygame.model.GameMode
import java.util.*
import kotlin.concurrent.schedule


class LobbyActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityLobbyBinding
    private var tts: TextToSpeech? = null
    private var isTTsEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lobby)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val wic = WindowInsetsControllerCompat(window, window.decorView)
            wic.isAppearanceLightStatusBars = true
            window.statusBarColor = ContextCompat.getColor(this, R.color.background_green)
        }

        setOnClickListeners()
        tts = TextToSpeech(this, this)
        Timer().schedule(1000) {
            runOnUiThread {
                speakOut()
            }
        }
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

    private fun speakOut() {
        if (isTTsEnable) {
            tts?.speak(getString(R.string.lobby_speak_select_game_mode), TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS) {
            tts?.let {
                val result = it.setLanguage(Locale.US)

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language specified is not supported!")
                } else {
                    isTTsEnable = true
                }
            }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        tts?.let {
            it.stop()
            it.shutdown()
        }
        super.onDestroy()
    }
}