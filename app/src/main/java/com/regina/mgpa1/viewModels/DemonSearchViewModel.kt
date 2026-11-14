package com.regina.mgpa1.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

//==CLASSES

// A class that stores states and functions related to the search function (In Home section)
class DemonSearchViewModel(
    // Need this to access the level list
    val listViewModel: DemonListViewModel
) : ViewModel() {
    // General rule for states: Private mutable inside, read-only outside.

    // The text that has been typed into the text field (search bar)
    private val _searchText = MutableStateFlow(value = "")
    val searchText = _searchText.asStateFlow()

    // Boolean flag to indicate whether the user is typing in the search bar
    private val _isSearching = MutableStateFlow(value = false)
    val isSearching = _isSearching.asStateFlow()

    // Access the view model and get the level list
    private val _levelList = listViewModel.levelList
    val levelList = searchText
        .combine(flow = _levelList) { text, levelList ->
            // This block will be called when the search text or level state changes

            if (text.isBlank()) {
                // If the query is blank, just show all levels in the list
                levelList
            } else {
                // If the query is not blank, filter the levelList and only show levels
                // who match the search query accordingly
                levelList.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }

    // Function to be called when the search text changes
    fun onSearchTextChanged(newText: String) {
        _searchText.value = newText
        _isSearching.value = newText.isNotBlank()
    }

    // Define ViewModel factory
    // As we want to pass the DemonListViewModel into the DemonSearchViewModel's
    // constructor, we need to create a factory to specify how we want that parameter
    // to be passed in. So we override the create function of the searchViewModel
    // and write a custom one that allows us to pass the listViewModel in.
    companion object {
        fun factory(
            listViewModel: DemonListViewModel
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DemonSearchViewModel(
                        listViewModel = listViewModel
                    ) as T
                }
            }
    }
}
