package com.homermemorygame.ui

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.homermemorygame.R
import com.homermemorygame.databinding.ActivityMemoryGameBinding
import com.homermemorygame.model.GameMode
import com.homermemorygame.model.MemoryGameCard
import com.homermemorygame.ui.viewmodel.MemoryGameViewModel
import com.homermemorygame.util.EventObserver
import com.homermemorygame.util.ItemOffsetDecoration
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.concurrent.schedule


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
    private val adapter = MemoryGameCardRecyclerViewAdapter(this, this)
    private val viewModel by viewModel<MemoryGameViewModel> {
        parametersOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_memory_game)

        intent?.extras?.let {
            gameMode = it.getParcelable(GAME_MODE_KEY) ?: GameMode.THREE_BY_FOUR
        }

        viewModel.getCardsList(gameMode)
        subscribe()
        setupView()
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val wic = WindowInsetsControllerCompat(window, window.decorView)
            wic.isAppearanceLightStatusBars = true
            window.statusBarColor = ContextCompat.getColor(this, R.color.background_green)
        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.rvMemoryGame.layoutManager = GridLayoutManager(this, gameMode.horizontal)
        val itemDecoration = ItemOffsetDecoration(this, R.dimen.item_offset)
        binding.rvMemoryGame.addItemDecoration(itemDecoration)
    }

    private fun subscribe() {
        viewModel.cardsListLiveData.observe(this, EventObserver {
            adapter.setData(it)
            binding.rvMemoryGame.adapter = adapter
        })
        viewModel.isAdapterClickableLiveData.observe(this, EventObserver {
            adapter.isClickable = it
        })

        viewModel.gameFinishedLiveData.observe(this, EventObserver {
            Toast.makeText(this, "You Won", Toast.LENGTH_SHORT).show()
        })
        viewModel.updatedCardsListLiveData.observe(this, EventObserver {
            setupLottieAnimation()
            Timer().schedule(600) {
                runOnUiThread {
                    adapter.setData(it)
                }
            }
        })
        viewModel.wrongCardSelectedLiveData.observe(this, EventObserver {
            Timer().schedule(2000) {
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                    adapter.isClickable = true
                }
            }
        })
    }

    private fun setupLottieAnimation() {
        binding.apply {
            lavSuccessAnimation.visibility = View.VISIBLE
            lavSuccessAnimation.playAnimation()
            lavSuccessAnimation.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    lavSuccessAnimation.visibility = View.GONE
                    tvMemoryGameTitle.visibility = View.VISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun memoryGameCardClickListener(memoryGameCard: MemoryGameCard, adapterPosition: Int) {
        viewModel.checkForMatchingCards(memoryGameCard.id, adapterPosition)
    }
}