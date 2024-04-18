package com.misoramen.hobbyapp.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.misoramen.hobbyapp.model.User
import org.json.JSONException
import org.json.JSONObject

class UserViewModel(val app: Application): AndroidViewModel(app) {
    val usersLD = MutableLiveData<User?>()
    var messageLD = MutableLiveData<String>()
    var resultLD = MutableLiveData<String>()
    val userLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    fun saveUserId(idUser: Int?) {
        if (idUser != null) {
            val sharedPref = app.getSharedPreferences("com.torimiso.hobbyapp", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("ID", idUser.toString())
            editor.apply()
        }
    }

    fun loadUserId(): String? {
        val sharedPref = app.getSharedPreferences("com.torimiso.hobbyapp", Context.MODE_PRIVATE)
        return sharedPref.getString("ID", null)
    }

    fun login(username: String, password: String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://anmp160721029.000webhostapp.com/login.php"

        val params = HashMap<String, String>()
        params["username"] = username
        params["password"] = password

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {response ->
                Log.d("show_volley", response)
                try {
                    val jsonObject = JSONObject(response)
                    val result = jsonObject.getString("result")
                    val userJson = jsonObject.getJSONObject("data")

                    if (result == "success") {
                        // Extract user data using Gson
                        val sType = object : TypeToken<User>() {}.type
                        val user = Gson().fromJson<User>(userJson.toString(), sType)
                        usersLD.value = user
                        saveUserId(user.id)
                        Log.d("show_volley", "Login successful!")
                    } else {
                        Log.w("show_volley", "Login failed: $result")
                    }
                } catch (e: JSONException) {
                    Log.e("show_volley", "Error parsing JSON response: $e")
                } finally {
                    // Set usersLD.value to null if either success or exception occurs
                    usersLD.value = null
                }
            },
            {
                Log.e("show_voley", it.toString())
            }
        ){
            override fun getParams(): MutableMap<String, String>? {
                return params
            }
        }
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    fun createAccount(username: String, firstName: String, lastName: String, email: String, password: String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://anmp160721029.000webhostapp.com/add_user.php"

        val params = HashMap<String, String>()
        params["username"] = username
        params["password"] = password
        params["email"] = email
        params["firstName"] = firstName
        params["lastName"] = lastName

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {response ->
                Log.d("show_volley", response)
                try {
                    val jsonObject = JSONObject(response)
                    val result = jsonObject.getString("result")
                    val message = jsonObject.getString("message")

                    if (result == "success") {
                        // Extract user data using Gson
                        val sType = object : TypeToken<User>() {}.type
                        resultLD.value = result
                        messageLD.value = message
                        Log.d("show_volley", message)
                    } else {
                        resultLD.value = result
                        messageLD.value = message
                        Log.w("show_volley", "Login failed: $result")
                    }
                } catch (e: JSONException) {
                    Log.e("show_volley", "Error parsing JSON response: $e")
                }
            },
            {
                Log.e("show_voley", it.toString())
            }
        ){
            override fun getParams(): MutableMap<String, String>? {
                return params
            }
        }
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    fun getAccount(){
        userLoadErrorLD.value = false
        loadingLD.value = false

        queue = Volley.newRequestQueue(getApplication())
        val url = "https://anmp160721029.000webhostapp.com/get_user.php"

        val idUser = loadUserId()

        if (idUser != null){
            val params = HashMap<String, String>()
            params["idUsers"] = idUser

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                {response ->
                    Log.d("show_volley", response)
                    try {
                        val jsonObject = JSONObject(response)
                        val result = jsonObject.getString("result")
                        val userJson = jsonObject.getJSONObject("data")

                        if (result == "success") {
                            // Extract user data using Gson
                            loadingLD.value = false
                            val sType = object : TypeToken<User>() {}.type
                            val user = Gson().fromJson<User>(userJson.toString(), sType)
                            usersLD.value = user
                            Log.d("show_volley", "Data fetch success" + user)
                        } else {
                            loadingLD.value = false
                            userLoadErrorLD.value = false
                            Log.w("show_volley", "Data fetch failed")
                        }
                    } catch (e: JSONException) {
                        Log.e("show_volley", "Error parsing JSON response: $e")
                    }
                },
                {
                    Log.e("show_voley", it.toString())
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

    fun updateProfile(firstName: String, lastName: String, password: String){
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://anmp160721029.000webhostapp.com/update_user.php"

        val idUser = loadUserId()

        if (idUser != null){
            val params = HashMap<String, String>()
            params["idUsers"] = idUser
            params["newPass"] = password
            params["firstName"] = firstName
            params["lastName"] = lastName

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                {response ->
                    Log.d("show_volley", response)
                    try {
                        val jsonObject = JSONObject(response)
                        val result = jsonObject.getString("result")
                        val message = jsonObject.getString("message")

                        if (result == "success") {
                            // Extract user data using Gson
                            val sType = object : TypeToken<User>() {}.type
                            resultLD.value = result
                            messageLD.value = message
                            Log.d("show_volley", "Update profile success")
                        } else {
                            resultLD.value = result
                            messageLD.value = message
                            Log.w("show_volley", "Update data failed")
                        }
                    } catch (e: JSONException) {
                        Log.e("show_volley", "Error parsing JSON response: $e")
                    }
                },
                {
                    Log.e("show_voley", it.toString())
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
}