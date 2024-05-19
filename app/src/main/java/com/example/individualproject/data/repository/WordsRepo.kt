package com.example.individualproject.data.repository

import androidx.room.Query
import androidx.room.util.query
import com.example.individualproject.data.db.WordsDao
import com.example.individualproject.data.model.Words
import kotlinx.coroutines.flow.Flow

class WordsRepo(
    private val dao: WordsDao
) {
    fun getAllWords(query: String): Flow<List<Words>> = dao.getAll(query)

    fun getAllCompletedWords(query: String): Flow<List<Words>> = dao.getAllCompleted(query)

    fun getWordsById(id: Int): Words? = dao.getWordsById(id)

    fun addWords(words: Words) = dao.addWords(words)

    fun updateWords(words: Words) = dao.updateWords(words)

    fun deleteWords(words: Words) = dao.deleteWords(words)
}