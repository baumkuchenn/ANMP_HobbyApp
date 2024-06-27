package com.misoramen.hobbyapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.misoramen.hobbyapp.model.HobbyDatabase
import com.misoramen.hobbyapp.model.News
import com.misoramen.hobbyapp.model.NewsWithAuthor
import com.misoramen.hobbyapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NewsViewModel(app: Application): AndroidViewModel(app), CoroutineScope {
    val newsLD = MutableLiveData<ArrayList<NewsWithAuthor>>()
    val newsLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()

    fun loadNews(){
        newsLoadErrorLD.value = false
        loadingLD.value = false

        launch {
            val db = buildDb(getApplication())
            val result = db.hobbyDao().selectAllNews()
            newsLD.value = result as ArrayList<NewsWithAuthor>
        }
    }

    fun addNews(list:List<News>){
        launch{
            val db = HobbyDatabase.buildDatabase(
                getApplication()
            )
            db.hobbyDao().insertNews(*list.toTypedArray())
        }
    }
    fun getCertainNews(idNews: Int){
        newsLoadErrorLD.value = false
        loadingLD.value = false

        launch {
            val db = buildDb(getApplication())
            val result = db.hobbyDao().selectCertainNews(idNews)
            newsLD.value = result as ArrayList<NewsWithAuthor>
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}