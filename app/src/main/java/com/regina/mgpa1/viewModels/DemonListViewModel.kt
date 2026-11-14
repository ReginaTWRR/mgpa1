package com.regina.mgpa1.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.regina.mgpa1.logic.GetLevelList
import com.regina.mgpa1.logic.Level
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//==CLASSES

// A class that does the API call and stores the entire demon list. This is to have
// a centralised access point for the list within the app instead of having multiple
// classes make an API call whenever they need something from the list.
class DemonListViewModel : ViewModel() {
    private val _levelList = MutableStateFlow<List<Level>>(value = emptyList())
    val levelList = _levelList.asStateFlow()

    init {
        LoadLevelList()
    }

    fun getRandomLevel(): Level? {
        return _levelList.value.randomOrNull()
    }

    private fun LoadLevelList() {
        viewModelScope.launch {
            // Get the list of all levels using the API layer's GetLevelList() function
            _levelList.value = GetLevelList()
        }
    }
}
