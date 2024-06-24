package com.misoramen.hobbyapp.util

import android.content.Context
import com.misoramen.hobbyapp.model.HobbyDatabase

val DB_NAME = "anmp_hobby"

//Panggil dari method di ToDoDatabase
fun buildDb(context: Context): HobbyDatabase {
    val db = HobbyDatabase.buildDatabase(context)
    return db
}