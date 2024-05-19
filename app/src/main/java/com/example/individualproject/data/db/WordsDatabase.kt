package com.example.individualproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.individualproject.core.Convertor
import com.example.individualproject.data.model.Words

@Database(entities = [Words::class], version = 2)
@TypeConverters(Convertor::class)
abstract class WordsDatabase: RoomDatabase() {
    abstract fun wordsDao(): WordsDao

    companion object {
        const val NAME = "my_words_database"
    }
}