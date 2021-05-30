package com.homermemorygame.ui.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.homermemorygame.R


class CustomCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var cardImageVisible: ImageView
    private var cardRealImage: Int = 0

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
            val objectAnimator1 = ObjectAnimator.ofFloat(cardImageVisible, "scaleX", 1f, 0f)
            val objectAnimator2 = ObjectAnimator.ofFloat(cardImageVisible, "scaleX", 0f, 1f)
            objectAnimator1.interpolator = DecelerateInterpolator()
            objectAnimator2.interpolator = AccelerateDecelerateInterpolator()
            objectAnimator1.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    cardImageVisible.tag = cardRealImage
                    cardImageVisible.setImageResource(cardRealImage)
                    objectAnimator2.start()
                }
            })
            objectAnimator1.start()
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

    fun revertCardStateWithAnimation() {
        val objectAnimator1 = ObjectAnimator.ofFloat(cardImageVisible, "scaleX", 1f, 0f)
        val objectAnimator2 = ObjectAnimator.ofFloat(cardImageVisible, "scaleX", 0f, 1f)
        objectAnimator1.interpolator = DecelerateInterpolator()
        objectAnimator2.interpolator = AccelerateDecelerateInterpolator()
        objectAnimator1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                cardImageVisible.tag = R.drawable.card_back
                cardImageVisible.setImageResource(R.drawable.card_back)
                objectAnimator2.start()
            }
        })
        objectAnimator1.start()
    }
}