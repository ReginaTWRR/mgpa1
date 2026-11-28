package com.regina.mgpa1.viewModels

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//==CLASSES

// A class to store data related to the Game Section
class GameSectionViewModel : ViewModel() {
    private val _currentPage = MutableStateFlow<GamesPage?>(value = null)
    val currentPage = _currentPage.asStateFlow()

    fun onPageChanged(newPage: GamesPage) {
        _currentPage.value = newPage
    }
}

//==CLASSES

// A data class that defines a games page
data class GamesPage(
    val code: PageCode,
    val content: @Composable () -> Unit
)

// A data class that defines a game
data class Game(
    val name: String,
    val page: GamesPage
)

// An enum class that stores a code for each page, used to reliably identify a page
enum class PageCode {
    OVERVIEW, HIGHERLOWER
}
