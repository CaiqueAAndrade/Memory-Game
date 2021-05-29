package com.homermemorygame.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView


class ItemOffsetDecoration(val context: Context, @DimenRes val itemOffsetId: Int): RecyclerView.ItemDecoration() {

    private var mItemOffset = 0

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        mItemOffset = context.resources.getDimensionPixelSize(itemOffsetId)
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }
}