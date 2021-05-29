package com.homermemorygame.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.homermemorygame.R


class CustomCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var cardImageVisible: ImageView
    private var cardRealImage: Int = 0
    private val animFadeOut: Animation = AnimationUtils.loadAnimation(context, R.anim.fadeout)
    private val animFadeIn: Animation = AnimationUtils.loadAnimation(context, R.anim.fadein)

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_card, this, true)

        cardImageVisible = findViewById(R.id.iv_custom_card)

        attrs?.let {
            val attributes =
                context.obtainStyledAttributes(it, R.styleable.CustomCard, 0, 0)

            cardImageVisible.setImageResource(
                attributes.getResourceId(
                    R.styleable.CustomCard_customCardImage,
                    R.drawable.card_back
                )
            )

            attributes.recycle()
        }
    }

    fun saveCardRealImage(imageString: String) {
        cardRealImage =
            context.resources.getIdentifier(imageString, "drawable", context.packageName)
    }

    fun showCard() {
        if (cardImageVisible.tag == R.drawable.card_back) {
            animFadeOut.reset()
            cardImageVisible.clearAnimation()
            cardImageVisible.startAnimation(animFadeOut)

            animFadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {}
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {
                    cardImageVisible.tag = cardRealImage
                    cardImageVisible.setImageResource(cardRealImage)
                    animFadeIn.reset()
                    cardImageVisible.clearAnimation()
                    cardImageVisible.startAnimation(animFadeIn)
                }
            })
        }
    }

    fun setRealCardImage() {
        cardImageVisible.tag = cardRealImage
        cardImageVisible.setImageResource(cardRealImage)
    }

    fun revertCardState() {
        cardImageVisible.tag = R.drawable.card_back
        cardImageVisible.setImageResource(R.drawable.card_back)
    }

}