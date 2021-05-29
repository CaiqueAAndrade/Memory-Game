package com.homermemorygame.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.homermemorygame.R
import com.homermemorygame.databinding.ActivityMemoryGameBinding
import com.homermemorygame.model.GameMode
import com.homermemorygame.model.MemoryGameCard
import com.homermemorygame.util.Utils

class MemoryGameActivity : AppCompatActivity(),
    MemoryGameCardRecyclerViewAdapter.MemoryGameCardOnClickListener {

    companion object {
        private const val GAME_MODE_KEY = "game_mode_key"

        fun start(context: Context, gameMode: GameMode) {
            val intent = Intent(context, MemoryGameActivity::class.java)
            intent.putExtra(GAME_MODE_KEY, gameMode as Parcelable)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityMemoryGameBinding
    private lateinit var gameMode: GameMode
    private lateinit var memoryGameCards: List<MemoryGameCard>
    private val adapter = MemoryGameCardRecyclerViewAdapter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_memory_game)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val wic = WindowInsetsControllerCompat(window, window.decorView)
            wic.isAppearanceLightStatusBars = true
            window.statusBarColor = Color.WHITE
        }

        intent?.extras?.let {
            gameMode = it.getParcelable(GAME_MODE_KEY) ?: GameMode.THREE_BY_FOUR
        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        memoryGameCards = Gson().fromJson(
            Utils.loadJSONFromAsset(this, "memory_game_cards.json"),
            object : TypeToken<List<MemoryGameCard>>() {}.type
        ) ?: listOf()

        binding.rvMemoryGame.layoutManager = GridLayoutManager(this, gameMode.horizontal)

        if (this::memoryGameCards.isInitialized) {
            adapter.setData(memoryGameCards as ArrayList<MemoryGameCard>)
            binding.rvMemoryGame.adapter = adapter
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun memoryGameCardClickListener(memoryGameCard: MemoryGameCard) {
        Toast.makeText(this, memoryGameCard.name, Toast.LENGTH_SHORT).show()
    }
}