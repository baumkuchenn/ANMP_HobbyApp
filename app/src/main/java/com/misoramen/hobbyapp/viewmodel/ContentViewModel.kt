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
import com.misoramen.hobbyapp.model.Contents
import com.misoramen.hobbyapp.model.News
import com.misoramen.hobbyapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class ContentViewModel(app: Application): AndroidViewModel(app), CoroutineScope {
    val contentLD = MutableLiveData<List<Contents>>()
    val countLD = MutableLiveData<String>()
    val contentLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()

    fun loadContent(idNews: Int, index: Int){
        contentLoadErrorLD.value = false
        loadingLD.value = false

        launch {
            val db = buildDb(getApplication())
            contentLD.postValue(db.hobbyDao().selectNewsContent(idNews, index))
            countLD.postValue(db.hobbyDao().selectPageContent(idNews))
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}