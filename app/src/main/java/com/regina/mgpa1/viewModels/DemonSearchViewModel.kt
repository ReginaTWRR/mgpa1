package com.regina.mgpa1.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//==CLASSES

// A class that stores states and functions related to the search function (In Home section)
class DemonSearchViewModel : ViewModel() {
    // General rule for states: Private mutable inside, read-only outside.

    // The text that has been typed into the text field (search bar)
    private val _searchText = MutableStateFlow(value = "")
    val searchText = _searchText.asStateFlow()

    // Boolean flag to indicate whether the user is typing in the search bar
    private val _isSearching = MutableStateFlow(value = false)
    val isSearching = _isSearching.asStateFlow()
}
