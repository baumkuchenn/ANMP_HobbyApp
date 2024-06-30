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
import com.misoramen.hobbyapp.model.HobbyDao
import com.misoramen.hobbyapp.model.User
import com.misoramen.hobbyapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

class UserViewModel(val app: Application): AndroidViewModel(app), CoroutineScope {
    val usersLD = MutableLiveData<User?>()
    var messageLD = MutableLiveData<String>()
    var resultLD = MutableLiveData<String>()
    val userLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()

    fun saveUserId(idUser: String?) {
        if (idUser != null) {
            val sharedPref = app.getSharedPreferences("com.torimiso.hobbyapp", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("ID", idUser)
            editor.apply()
        }
    }

    fun loadUserId(): String? {
        val sharedPref = app.getSharedPreferences("com.torimiso.hobbyapp", Context.MODE_PRIVATE)
        return sharedPref.getString("ID", null)
    }

    fun login(user: User){
        userLoadErrorLD.value = false
        loadingLD.value = true

        launch {
            val db = buildDb(getApplication())
            val user = db.hobbyDao().loginUser(user.username, user.password)
            usersLD.postValue(user)
            if (usersLD.value != null){
                loadingLD.postValue(false)
                resultLD.postValue("success")
                messageLD.postValue("Login berhasil")
            } else {
                loadingLD.postValue(false)
                resultLD.postValue("ERROR")
                messageLD.postValue("Login gagal, username atau password salah")
            }
        }
    }

    fun createAccount(user: User){
        loadingLD.value = true
        launch {
            val db = buildDb(getApplication())
            usersLD.postValue(db.hobbyDao().getUsername(user.username))
            if (usersLD.value == null){
                db.hobbyDao().registerUser(user)
                loadingLD.postValue(false)
                resultLD.postValue("success")
                messageLD.postValue("Pendaftaran berhasil")
            } else {
                loadingLD.postValue(false)
                resultLD.postValue("ERROR")
                messageLD.postValue("Gagal daftar karena username sudah terpakai")
            }
        }
    }

    fun getAccount(idUser: String?){
        userLoadErrorLD.value = false
        loadingLD.value = true

        launch {
            val db = buildDb(getApplication())
            usersLD.postValue(db.hobbyDao().getCertainUser(idUser!!))
            loadingLD.postValue(false)
        }
    }

    fun updateProfile(user: User){
        launch {
            val db = buildDb(getApplication())
            db.hobbyDao().update(user.firstName, user.lastName, user.password, user.id.toString())
            loadingLD.postValue(false)
            resultLD.postValue("success")
            messageLD.postValue("Profile berhasil diubah")
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}