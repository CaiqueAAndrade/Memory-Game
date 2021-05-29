package com.homermemorygame.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.homermemorygame.model.MemoryGameCard
import com.homermemorygame.util.Utils

class MemoryGameRepository(private val context: Context) {

    fun getMemoryCardsList(): ArrayList<MemoryGameCard> {
        return Gson().fromJson(
            Utils.loadJSONFromAsset(context, "memory_game_cards.json"),
            object : TypeToken<ArrayList<MemoryGameCard>>() {}.type
        ) ?: arrayListOf()
    }
}