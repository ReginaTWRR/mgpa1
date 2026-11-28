package com.regina.mgpa1.viewModels

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.regina.mgpa1.games.higherLower.MainHigherLowerGame
import com.regina.mgpa1.logic.Playable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//==CLASSES

// A class that stores data related to the Higher Lower game
class HigherLowerViewModel : ViewModel(), Playable {
    private val _game = MutableStateFlow<Game?>(value = null)
    val game = _game.asStateFlow()

    private val _currentPage = MutableStateFlow<HigherLowerPage?>(value = null)
    val currentPage = _currentPage.asStateFlow()

    var mainPage: HigherLowerPage? = null
    var lostPage: HigherLowerPage? = null

    // MainHigherLowerGame gets to update the streak,
    // LostHigherLowerGame just reads the value
    private val _streak = MutableStateFlow(value = 0)
    val streak = _streak.asStateFlow()

    override fun enterGame() {
        // Use .let so that the block only runs when mainPage is not null
        mainPage?.let {
            onPageChanged(newPage = it)
        }
    }

    fun initGame(returnOverviewPage: () -> Unit) {
        _game.value = Game(
            name = "HIGHER OR LOWER",
            page = GamesPage(
                code = PageCode.HIGHERLOWER,
                content = {
                    MainHigherLowerGame(
                        returnOverviewPage = returnOverviewPage
                    ).SectionLayout()
                }
            ),
            onEnter = {
                mainPage?.let {
                    onPageChanged(newPage = it)
                }
            }
        )
    }

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
