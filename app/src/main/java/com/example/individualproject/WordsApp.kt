package com.example.individualproject

import android.app.Application
import androidx.room.Room
import com.example.individualproject.data.db.WordsDatabase
import com.example.individualproject.data.repository.WordsRepo

class WordsApp: Application() {
    lateinit var repo: WordsRepo
    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(
            this,
            WordsDatabase::class.java,
            WordsDatabase.NAME
        ).fallbackToDestructiveMigration().build()

        repo = WordsRepo(db.wordsDao())
    }
}