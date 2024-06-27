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

    fun login(user: User) {
        userLoadErrorLD.value = false
        loadingLD.value = true

        launch {
            val db = buildDb(getApplication())
            val loggedInUser = db.hobbyDao().loginUser(user.username, user.password)
            if (loggedInUser != null) {
                saveUserId(loggedInUser.id)
                loadingLD.postValue(false)
                resultLD.postValue("success")
                messageLD.postValue("Login berhasil")
                usersLD.postValue(loggedInUser) // Update LiveData on the main thread
            } else {
                loadingLD.postValue(false)
                resultLD.postValue("ERROR")
                messageLD.postValue("Login gagal, username atau password salah")
                usersLD.postValue(null) // Update LiveData on the main thread
            }
        }
    }


    fun createAccount(user: User){
        loadingLD.value = true
        launch {
            val db = buildDb(getApplication())
            usersLD.postValue(db.hobbyDao().getUsername(user.username))
            if (usersLD.value == null){
                val currentInstant = Instant.now()
                val currentZone = ZoneId.systemDefault()
                val localDateTime = currentInstant.atZone(currentZone).toLocalDateTime()
                val formattedDateTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

                val newUser = User(user.username, user.password, user.email, user.firstName, user.lastName, formattedDateTime)
                db.hobbyDao().registerUser(newUser)
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

    fun updateProfile(user: User){
        val idUser = loadUserId()
        launch {
            val db = buildDb(getApplication())
            db.hobbyDao().update(user.firstName, user.lastName, user.password, idUser!!)
            loadingLD.value = false
            resultLD.value = "success"
            messageLD.value = "Profile berhasil diubah"
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}