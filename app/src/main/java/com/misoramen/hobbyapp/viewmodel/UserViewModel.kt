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
        userLoadErrorLD.value = false
        loadingLD.value = true

        launch {
            val db = buildDb(getApplication())
            usersLD.value = db.hobbyDao().loginUser(username, password)
            if (usersLD.value != null){
                saveUserId(usersLD.value!!.id)
                loadingLD.value = false
                resultLD.value = "success"
                messageLD.value = "Login berhasil"
            } else {
                loadingLD.value = false
                resultLD.value = "ERROR"
                messageLD.value = "Login gagal, username atau password salah"
            }
        }
    }

    fun createAccount(username: String, firstName: String, lastName: String, email: String, password: String){
        loadingLD.value = true
        launch {
            val db = buildDb(getApplication())
            usersLD.value = db.hobbyDao().getUsername(username)
            if (usersLD.value == null){
                val currentInstant = Instant.now()
                val currentZone = ZoneId.systemDefault()
                val localDateTime = currentInstant.atZone(currentZone).toLocalDateTime()
                val formattedDateTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

                val newUser = User(username, password, email, firstName, lastName, formattedDateTime)
                db.hobbyDao().registerUser(newUser)
                loadingLD.value = false
                resultLD.value = "success"
                messageLD.value = "Pendaftaran berhasil"
            } else {
                loadingLD.value = false
                resultLD.value = "ERROR"
                messageLD.value = "Gagal daftar karena username sudah terpakai"
            }
        }
    }

    fun getAccount(){
        userLoadErrorLD.value = false
        loadingLD.value = true

        val idUser = loadUserId()
        launch {
            val db = buildDb(getApplication())
            usersLD.value = db.hobbyDao().getCertainUser(idUser!!)
            loadingLD.value = false
        }
    }

    fun updateProfile(firstName: String, lastName: String, password: String){
        val idUser = loadUserId()
        launch {
            val db = buildDb(getApplication())
            db.hobbyDao().update(firstName, lastName, password, idUser!!)
            loadingLD.value = false
            resultLD.value = "success"
            messageLD.value = "Profile berhasil diubah"
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}