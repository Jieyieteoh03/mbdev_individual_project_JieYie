package com.example.individualproject.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Query
import com.example.individualproject.WordsApp
import com.example.individualproject.data.model.Words
import com.example.individualproject.data.repository.WordsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: WordsRepo
): ViewModel() {
    val sortTitle: MutableLiveData<String> = MutableLiveData("title")
    val sortOrder: MutableLiveData<String> = MutableLiveData("ASC")
    private val _query: MutableLiveData<String> = MutableLiveData("")
    val query: LiveData<String> = _query
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun setQuery(query: String) = _query.postValue(query)

    fun getAll(): Flow<List<Words>> = repo.getAllWords(query.value!!)

    fun getAllCompleted(): Flow<List<Words>> = repo.getAllCompletedWords(query.value!!)

    fun sortWord(words: List<Words>): List<Words> {
        return if (sortOrder.value == "ASC") {
            if(sortTitle.value == "title") words.sortedBy { it.title }
            else words.sortedBy { it.dateCreated }
        } else {
            if(sortTitle.value == "title") words.sortedByDescending { it.title }
            else words.sortedByDescending { it.dateCreated }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
               HomeViewModel(
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WordsApp).repo
                )
            }
        }
    }
}
