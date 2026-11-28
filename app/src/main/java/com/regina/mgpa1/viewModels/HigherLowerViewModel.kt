package com.regina.mgpa1.viewModels

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//==CLASSES

// A class that stores data related to the Higher Lower game
class HigherLowerViewModel : ViewModel() {
    private val _currentPage = MutableStateFlow<HigherLowerPage?>(value = null)
    val currentPage = _currentPage.asStateFlow()

    var mainPage: HigherLowerPage? = null
    var lostPage: HigherLowerPage? = null

    // MainHigherLowerGame gets to update the streak,
    // LostHigherLowerGame just reads the value
    private val _streak = MutableStateFlow(value = 0)
    val streak = _streak.asStateFlow()

    fun onPageChanged(newPage: HigherLowerPage) {
        _currentPage.value = newPage
    }

    fun onStreakIncreased() {
        ++_streak.value
    }

    fun onStreakReset() {
        _streak.value = 0
    }
}

//==CLASSES

// A data class that defines a games page
data class HigherLowerPage(
    val code: HigherLowerPageCode,
    val content: @Composable () -> Unit
)

// An enum class that stores a code for each page, used to reliably identify a page
enum class HigherLowerPageCode {
    MAINHIGHERLOWER, LOSTHIGHERLOWER
}

// An enum class that defines the choices a user can make
enum class UserChoice {
    NONE, HIGHER, LOWER
}
