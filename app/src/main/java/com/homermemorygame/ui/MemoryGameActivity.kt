package com.homermemorygame.ui

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.os.SystemClock
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.homermemorygame.R
import com.homermemorygame.databinding.ActivityMemoryGameBinding
import com.homermemorygame.extensions.millisToMinutes
import com.homermemorygame.model.GameMode
import com.homermemorygame.model.MemoryGameCard
import com.homermemorygame.ui.viewmodel.MemoryGameViewModel
import com.homermemorygame.util.EventObserver
import com.homermemorygame.util.ItemOffsetDecoration
import kotlinx.android.synthetic.main.bottom_sheet_success.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.concurrent.schedule


class MemoryGameActivity : AppCompatActivity(),
    MemoryGameCardRecyclerViewAdapter.MemoryGameCardOnClickListener,
    TextToSpeech.OnInitListener {

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
    private val successMessageBottomSheetDialog by lazy { BottomSheetDialog(this) }
    private lateinit var successMessageLayout: View
    private val adapter = MemoryGameCardRecyclerViewAdapter(this, this)
    private var tts: TextToSpeech? = null
    private var isTTsEnable = false
    private val viewModel by viewModel<MemoryGameViewModel> {
        parametersOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_memory_game)

        intent?.extras?.let {
            gameMode = it.getParcelable(GAME_MODE_KEY) ?: GameMode.THREE_BY_FOUR
        }

        tts = TextToSpeech(this, this)
        viewModel.getCardsList(gameMode)
        subscribe()
        setupView()
        setupSuccessBottomSheetDialog()
        Timer().schedule(1000) {
            runOnUiThread {
                speakOut(getString(R.string.memory_game_speak_match_cards_description))
            }
        }
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
            viewModel.shouldStartTimer(false)
            Timer().schedule(1000) {
                runOnUiThread {
                    setLoginBottomSheetState()
                    speakOut(getString(R.string.memory_game_speak_game_completed_description))
                }
            }
            val time =
                (SystemClock.elapsedRealtime() - binding.chGameChronometer.base).millisToMinutes()
            successMessageBottomSheetDialog.tv_success_message_description.text =
                getString(R.string.success_bottom_sheet_time_played_description).replace(
                    "%a",
                    time
                )
        })
        viewModel.updatedCardsListLiveData.observe(this, EventObserver {
            setupLottieAnimation()
            Timer().schedule(600) {
                runOnUiThread {
                    adapter.setData(it)
                    speakOut(getString(R.string.memory_game_speak_match_successful_description))
                }
            }
        })
        viewModel.wrongCardSelectedLiveData.observe(this, EventObserver {
            Timer().schedule(2000) {
                runOnUiThread {
                    adapter.revertNotMatchCards(it)
                    adapter.isClickable = true
                    speakOut(getString(R.string.memory_game_speak_wrong_match_description))
                }
            }
        })
        viewModel.shouldStartTimerLiveData.observe(this, EventObserver {
            if (it) {
                binding.chGameChronometer.start()
            } else {
                binding.chGameChronometer.stop()
            }
        })
        viewModel.speakCardNameLiveData.observe(this, EventObserver {
            speakOut(it)
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

    private fun setLoginBottomSheetState() {
        if (successMessageBottomSheetDialog.isShowing) {
            successMessageBottomSheetDialog.dismiss()
        } else {
            successMessageBottomSheetDialog.show()

            val behavior =
                BottomSheetBehavior.from(successMessageLayout.parent as FrameLayout)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setupSuccessBottomSheetDialog() {
        successMessageLayout =
            layoutInflater.inflate(R.layout.bottom_sheet_success, null)
        successMessageBottomSheetDialog.apply {
            this.setContentView(successMessageLayout)
            ib_success_message_close.setOnClickListener {
                setLoginBottomSheetState()
            }
            bt_success_message_try_again.setOnClickListener {
                binding.chGameChronometer.base = SystemClock.elapsedRealtime()
                viewModel.getCardsList(gameMode)
                setLoginBottomSheetState()
            }
        }
    }

    override fun memoryGameCardClickListener(memoryGameCard: MemoryGameCard, adapterPosition: Int) {
        viewModel.checkForMatchingCards(memoryGameCard, adapterPosition)
        viewModel.shouldStartTimer(true)
    }

    private fun speakOut(textToSpeech: String) {
        if (isTTsEnable) {
            tts?.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, "")
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