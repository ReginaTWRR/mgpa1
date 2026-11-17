package com.regina.mgpa1.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.regina.mgpa1.logic.Level
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//==CLASSES

// A class that stores states related to the Home Page
class HomePageViewModel(
    val listViewModel: DemonListViewModel
) : ViewModel() {
    // Store the random demon generated so that it does not refresh when exiting home page
    private val _generatedDemon = MutableStateFlow<Level?>(value = null)
    val generatedDemon = _generatedDemon.asStateFlow()

    init {
        // Watch the level list and get the random level once it has loaded
        viewModelScope.launch {
            listViewModel.levelList.collect { levelList ->
                if (_generatedDemon.value == null && levelList.isNotEmpty()) {
                    _generatedDemon.value = levelList.random()
                }
            }
        }
    }

    // Define ViewModel factory
    companion object {
        fun factory(
            listViewModel: DemonListViewModel
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomePageViewModel(
                        listViewModel = listViewModel
                    ) as T
                }
            }
    }
}
