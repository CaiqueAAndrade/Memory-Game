package com.homermemorygame.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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
        cardRealImage = context.resources.getIdentifier(imageString, "drawable", context.packageName)
    }

    fun showCard() {
        if (cardImageVisible.tag != cardRealImage) {
            cardImageVisible.setImageResource(cardRealImage)
        }
    }

    fun setRealCardImage() {
        cardImageVisible.setImageResource(cardRealImage)
    }

    fun revertCardState() {
        cardImageVisible.setImageResource(R.drawable.card_back)
    }

}