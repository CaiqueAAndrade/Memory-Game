package com.homermemorygame.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.homermemorygame.model.GameMode
import com.homermemorygame.model.MemoryGameCard
import com.homermemorygame.repository.MemoryGameRepository
import com.homermemorygame.util.Event
import kotlinx.coroutines.launch

class MemoryGameViewModel(
    mApplication: Application,
    private val repository: MemoryGameRepository
) : AndroidViewModel(mApplication) {

    private val completeList: ArrayList<MemoryGameCard> = arrayListOf()
    private var cardSelected: Int = 0
    private var lastAdapterPosition: Int? = null


    private val _cardsListMutableLiveData = MutableLiveData<Event<ArrayList<MemoryGameCard>>>()
    val cardsListLiveData: LiveData<Event<ArrayList<MemoryGameCard>>>
        get() = _cardsListMutableLiveData

    private val _updatedCardsListMutableLiveData =
        MutableLiveData<Event<ArrayList<MemoryGameCard>>>()
    val updatedCardsListLiveData: LiveData<Event<ArrayList<MemoryGameCard>>>
        get() = _updatedCardsListMutableLiveData

    private val _isAdapterClickableMutableLiveData = MutableLiveData<Event<Boolean>>()
    val isAdapterClickableLiveData: LiveData<Event<Boolean>>
        get() = _isAdapterClickableMutableLiveData

    private val _gameFinishedMutableLiveData = MutableLiveData<Event<Unit>>()
    val gameFinishedLiveData: LiveData<Event<Unit>>
        get() = _gameFinishedMutableLiveData

    private val _wrongCardSelectedMutableLiveData = MutableLiveData<Event<Unit>>()
    val wrongCardSelectedLiveData: LiveData<Event<Unit>>
        get() = _wrongCardSelectedMutableLiveData

    private val _shouldStartTimerMutableLiveData = MutableLiveData<Event<Boolean>>()
    val shouldStartTimerLiveData: LiveData<Event<Boolean>>
        get() = _shouldStartTimerMutableLiveData

    fun getCardsList(gameMode: GameMode) {
        viewModelScope.launch {
            completeList.clear()
            val memoryCards = repository.getMemoryCardsList()
            memoryCards.shuffle()
            val subList =
                memoryCards.subList(0, ((gameMode.horizontal * gameMode.vertical) / 2))
                    .toTypedArray()
            completeList.addAll(subList)
            completeList.addAll(subList)
            completeList.shuffle()
            _cardsListMutableLiveData.value = Event(completeList)
            _shouldStartTimerMutableLiveData.value = null
        }
    }

    fun shouldStartTimer(startTimer: Boolean) {
        if (_shouldStartTimerMutableLiveData.value == null || !startTimer) {
            _shouldStartTimerMutableLiveData.value = Event(startTimer)
        }
    }

    fun checkForMatchingCards(cardId: Int, adapterPosition: Int) {
        if (lastAdapterPosition != adapterPosition) {
            lastAdapterPosition = adapterPosition
        } else {
            return
        }
        if (cardSelected == 0) {
            cardSelected = cardId
        } else {
            _isAdapterClickableMutableLiveData.value = Event(false)
            if (cardSelected == cardId) {
                completeList.map { card ->
                    if (card.id == cardSelected) {
                        card.isCardMatch = true
                    }
                }
                if (completeList.all { card -> card.isCardMatch }) {
                    _gameFinishedMutableLiveData.value = Event(Unit)
                }
                _updatedCardsListMutableLiveData.value = Event(completeList)
                cardSelected = 0
                _isAdapterClickableMutableLiveData.value = Event(true)
            } else {
                cardSelected = 0
                _wrongCardSelectedMutableLiveData.value = Event(Unit)
            }
        }
    }
}