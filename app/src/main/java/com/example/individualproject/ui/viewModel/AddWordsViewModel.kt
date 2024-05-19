package com.example.individualproject.ui.viewModel

import android.text.Spannable.Factory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
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

class AddWordsViewModel (
    private val repo: WordsRepo
): ViewModel() {
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

    /*
        Checks whether the title and meaning are not null, if not will
        proceed with the function but if the title and meaning are empty will
        show a toast message
    */
    fun submit() {
        viewModelScope.launch (Dispatchers.IO){
            if (title.value != "" && meaning.value != "") {
                try {
                repo.addWords(Words(
                    title = title.value!!,
                    meaning = meaning.value!!,
                    synonyms = synonyms.value!!,
                    details = details.value!!,
                ))
                toastMessage("Added Succesfully")
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
                val repo = (this[APPLICATION_KEY] as WordsApp).repo
                AddWordsViewModel(repo)
            }
        }
    }
}