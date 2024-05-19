package com.example.individualproject.ui.viewModel

import android.text.Spannable.Factory
import android.util.Log
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

class UpdateWordsViewModel(
    private val repo: WordsRepo
): ViewModel() {
    private val _word: MutableLiveData<Words> = MutableLiveData()
    val word:LiveData<Words> = _word
    val title: MutableLiveData<String> = MutableLiveData("")
    val meaning: MutableLiveData<String> = MutableLiveData("")
    val synonyms: MutableLiveData<String> = MutableLiveData("")
    val details: MutableLiveData<String> = MutableLiveData("")
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    /*
       Represents a shared flow for emitting toast messages from the ViewModel to the UI.
       Allows multiple observers to receive emitted messages asynchronously
   */
    val toastMessage: MutableSharedFlow<String> = MutableSharedFlow()

    fun getWordById(id: Int) {
        viewModelScope.launch (Dispatchers.IO){
            try {
                _word.postValue(repo.getWordsById(id))
            } catch (e: Exception) { toastMessage("Error occured: ${e.message}") }

        }
    }

    fun setWord() {
        word.value?.let {
            title.value = it.title
            meaning.value = it.meaning
            synonyms.value = it.synonyms
            details.value = it.details
        }

    }

    /*
        Checks whether the title and meaning value are not null, also checks
        whether all the values are different from the data. If edited
        it will proceed with the function, but if the title and meaning are blank
        it will show a toast message
    */

    fun editWords() {
        viewModelScope.launch (Dispatchers.IO){
                if (title.value != "" &&
                    meaning.value != "" &&
                    (title.value != _word.value?.title ||
                            meaning.value != _word.value?.meaning ||
                            synonyms.value != _word.value?.synonyms ||
                            details.value != _word.value?.details)
                ) {
                    try {
                        repo.updateWords(word.value!!.copy(
                                title = title.value!!,
                                meaning = meaning.value!!,
                                synonyms = synonyms.value!!,
                                details = details.value!!
                        ))
                        toastMessage("Update Successfully")
                        finish.emit(Unit)

                    } catch (e: Exception) { toastMessage("Error occured: ${e.message}") }

                } else { toastMessage("Enter title and meaning") }
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
                UpdateWordsViewModel(repo)
            }
        }
    }
}


