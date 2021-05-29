package com.homermemorygame.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.homermemorygame.R
import com.homermemorygame.databinding.ItemCustomMemoryGameCardBinding
import com.homermemorygame.model.MemoryGameCard
import com.homermemorygame.ui.custom.CustomCard


class MemoryGameCardRecyclerViewAdapter(
    val context: Context,
    val listener: MemoryGameCardOnClickListener
) :
    RecyclerView.Adapter<MemoryGameCardRecyclerViewAdapter.MemoryGameCardViewHolder>() {

    private var items: ArrayList<MemoryGameCard> = arrayListOf()
    var isClickable = true

    interface MemoryGameCardOnClickListener {
        fun memoryGameCardClickListener(memoryGameCard: MemoryGameCard, adapterPosition: Int)
    }

    fun setData(memoryGameCards: ArrayList<MemoryGameCard>) {
        this.items = memoryGameCards
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemoryGameCardRecyclerViewAdapter.MemoryGameCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemCustomMemoryGameCardBinding>(
            inflater,
            R.layout.item_custom_memory_game_card,
            parent,
            false
        )
        parent.isMotionEventSplittingEnabled = false
        return MemoryGameCardViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MemoryGameCardRecyclerViewAdapter.MemoryGameCardViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class MemoryGameCardViewHolder(private val binding: ItemCustomMemoryGameCardBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(item: MemoryGameCard) {
            binding.apply {
                ivCard.saveCardRealImage(item.resource)
                if (item.isCardMatch.not()) {
                    ivCard.revertCardState()
                } else {
                    ivCard.setRealCardImage()
                }
            }
        }

        override fun onClick(p0: View?) {
            if (isClickable) {
                p0?.let {
                    (it as CustomCard).showCard()
                }
                listener.memoryGameCardClickListener(items[adapterPosition], adapterPosition)
            }
        }
    }
}