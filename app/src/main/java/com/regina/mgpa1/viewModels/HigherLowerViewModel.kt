package com.regina.mgpa1.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//==CLASSES

// A class that stores data related to the Higher Lower game
class HigherLowerViewModel : ViewModel() {
    // MainHigherLowerGame gets to update the streak,
    // LostHigherLowerGame just reads the value
    private val _streak = MutableStateFlow(value = 0)
    val streak = _streak.asStateFlow()

    fun onStreakIncreased() {
        ++_streak.value
    }

    fun onStreakReset() {
        _streak.value = 0
    }
}
