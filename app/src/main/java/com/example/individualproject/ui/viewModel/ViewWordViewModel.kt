package com.example.individualproject.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.individualproject.WordsApp
import com.example.individualproject.data.model.Words
import com.example.individualproject.data.repository.WordsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewWordViewModel(
    private val repo: WordsRepo
):ViewModel() {
    private val _word: MutableLiveData<Words> = MutableLiveData()
    val word: LiveData<Words> = _word
    val title: MutableLiveData<String> = MutableLiveData()
    val meaning: MutableLiveData<String> = MutableLiveData()
    val synonyms: MutableLiveData<String> = MutableLiveData()
    val details: MutableLiveData<String> = MutableLiveData()
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    /*
        Represents a shared flow for emitting toast messages from the ViewModel to the UI.
        Allows multiple observers to receive emitted messages asynchronously
    */
    val toastMessage: MutableSharedFlow<String> = MutableSharedFlow()

    fun getWordById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _word.postValue(repo.getWordsById(id))
        }
    }

    fun setWords() {
        word.value?.let {
            title.value = it.title
            meaning.value = it.meaning
            synonyms.value = it.synonyms
            details.value = it.details
        }

    }

    fun completeWords() {
        viewModelScope.launch(Dispatchers.IO) {
            val completeWord = word.value?.copy(isCompleted = true)
            try {
                repo.updateWords(completeWord!!)
                toastMessage("Added to Completed Words")
            } catch (e: Exception) {toastMessage("Error occured: ${e.message}")}
            finish.emit(Unit)
        }

    }

    fun deleteWords() {
        viewModelScope.launch (Dispatchers.IO){
            try {
                repo.deleteWords(word.value!!)
                toastMessage("Deleted Successfully")
            }catch (e: Exception) { toastMessage("Error occured: ${e.message}") }
            finish.emit(Unit)
        }
    }

    /*
      This function ensures that the message is emitted on the main thread,
      which is necessary for UI operations like displaying a Snackbar.
     */
    private suspend fun toastMessage(message: String){
        withContext(Dispatchers.Main){
            toastMessage.emit(message)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WordsApp).repo
                ViewWordViewModel(repo)
            }
        }
    }
}

