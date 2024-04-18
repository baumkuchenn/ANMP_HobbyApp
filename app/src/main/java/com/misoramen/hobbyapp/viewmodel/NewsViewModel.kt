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
import com.misoramen.hobbyapp.model.User
import org.json.JSONException
import org.json.JSONObject

class NewsViewModel(app: Application): AndroidViewModel(app) {
    val newsLD = MutableLiveData<ArrayList<News>>()
    val newsLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    fun loadNews(){
        newsLoadErrorLD.value = false
        loadingLD.value = false

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://anmp160721029.000webhostapp.com/get_news.php"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {
                loadingLD.value = false
                Log.d("show_volley", it)
                val sType = object : TypeToken<List<News>>(){}.type
                val result = Gson().fromJson<List<News>>(it, sType)
                newsLD.value = result as ArrayList<News>?
            },
            {
                Log.e("show_voley", it.toString())
                newsLoadErrorLD.value = false
                loadingLD.value = false
            }
        )
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    fun getCertainNews(idNews: String){
        newsLoadErrorLD.value = false
        loadingLD.value = false

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://anmp160721029.000webhostapp.com/get_certain_news.php"

        val params = HashMap<String, String>()
        params["idNews"] = idNews

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {
                loadingLD.value = false
                Log.d("show_volley", it)
                val sType = object : TypeToken<List<News>>(){}.type
                val result = Gson().fromJson<List<News>>(it, sType)
                newsLD.value = result as ArrayList<News>?
            },
            {
                Log.e("show_voley", it.toString())
                newsLoadErrorLD.value = false
                loadingLD.value = false
            }
        ){
            override fun getParams(): MutableMap<String, String>? {
                return params
            }
        }
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}