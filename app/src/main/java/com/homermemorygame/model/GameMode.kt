package com.homermemorygame.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class GameMode(val horizontal: Int, val vertical: Int) : Parcelable {
    THREE_BY_FOUR(3, 4),
    FIVE_BY_TWO(5, 2),
    FOUR_BY_FOUR(4, 4),
    FOUR_BY_FIVE(4, 5)
}