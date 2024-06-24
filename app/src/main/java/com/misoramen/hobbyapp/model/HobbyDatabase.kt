package com.misoramen.hobbyapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.misoramen.hobbyapp.util.DB_NAME

@Database(entities = arrayOf(User::class, News::class, Contents::class, Genre::class), version =  1)
abstract class HobbyDatabase:RoomDatabase() {
    abstract fun hobbyDao():HobbyDao
    companion object{
        @Volatile private var instance: HobbyDatabase ?= null
        private val LOCK = Any()

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                HobbyDatabase::class.java,
                DB_NAME)
                .build()
        operator fun invoke(context: Context) {
            if(instance!=null) {
                synchronized(LOCK) {
                    instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }
            }
        }
    }
}