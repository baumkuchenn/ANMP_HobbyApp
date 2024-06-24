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
import com.misoramen.hobbyapp.model.News
import com.misoramen.hobbyapp.model.NewsWithAuthor
import com.misoramen.hobbyapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NewsViewModel(app: Application): AndroidViewModel(app), CoroutineScope {
    val newsLD = MutableLiveData<List<NewsWithAuthor>>()
    val newsLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()

    fun loadNews(){
        newsLoadErrorLD.value = false
        loadingLD.value = false

        launch {
            val db = buildDb(getApplication())
            newsLD.postValue(db.hobbyDao().selectAllNews())
        }
    }

    fun getCertainNews(idNews: Int){
        newsLoadErrorLD.value = false
        loadingLD.value = false

        launch {
            val db = buildDb(getApplication())
            newsLD.postValue(db.hobbyDao().selectCertainNews(idNews))
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}