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
import org.json.JSONObject

class ContentViewModel(app: Application): AndroidViewModel(app) {
    val contentLD = MutableLiveData<ArrayList<Contents>>()
    val countLD = MutableLiveData<String>()
    val contentLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    fun loadContent(idNews: String, index: String){
        contentLoadErrorLD.value = false
        loadingLD.value = false

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://anmp160721029.000webhostapp.com/get_news_content.php"

        val params = HashMap<String, String>()
        params["idNews"] = idNews
        params["index"] = index

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {
                loadingLD.value = false
                Log.d("show_volley", it)
                val jsonObject = JSONObject(it)
                val count = jsonObject.getString("count")
                val resultJson = jsonObject.getJSONArray("data")

                val sType = object : TypeToken<List<Contents>>(){}.type
                val result = Gson().fromJson<List<Contents>>(resultJson.toString(), sType)
                contentLD.value = result as ArrayList<Contents>?
                countLD.value = count
                Log.d("show_volley", count)
            },
            {
                Log.e("show_voley", it.toString())
                contentLoadErrorLD.value = false
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
}