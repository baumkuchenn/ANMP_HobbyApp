package com.misoramen.hobbyapp.util

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.misoramen.hobbyapp.model.HobbyDatabase

val DB_NAME = "anmp_hobby"

//Panggil dari method di ToDoDatabase
fun buildDb(context: Context): HobbyDatabase {
    val db = HobbyDatabase.buildDatabase(context)
    return db
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE genre ADD COLUMN deskripsi TEXT DEFAULT 'Lorem Ipsum' not null")
    }
}
