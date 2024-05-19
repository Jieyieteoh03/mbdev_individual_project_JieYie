package com.example.individualproject.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.individualproject.data.model.Words
import kotlinx.coroutines.flow.Flow

@Dao
interface WordsDao {

    @Query("SELECT * FROM Words WHERE isCompleted = 0 AND title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun getAll(query: String): Flow<List<Words>>

    @Query("SELECT * FROM Words WHERE isCompleted = 1 AND title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun getAllCompleted(query: String): Flow<List<Words>>

    @Query("SELECT * FROM Words WHERE id = :id")
    fun getWordsById(id: Int): Words?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWords(words: Words)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWords(words: Words)

    @Delete
    fun deleteWords(words: Words)
}