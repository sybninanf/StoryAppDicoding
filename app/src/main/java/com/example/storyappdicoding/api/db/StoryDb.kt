package com.example.storyappdicoding.api.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storyappdicoding.api.models.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)

abstract class StoryDb : RoomDatabase() {
    abstract fun storyDao() : StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}