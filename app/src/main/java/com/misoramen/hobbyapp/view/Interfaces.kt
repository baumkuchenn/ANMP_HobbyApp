package com.misoramen.hobbyapp.view

import android.view.View
import com.misoramen.hobbyapp.model.User

interface RegisterUserClick{
    fun onRegisterUserClick(v: View)
}

interface NavLoginClick{
    fun onNavLoginClick(v: View)
}

interface LoginUserClick{
    fun onLoginUserClick(v: View)
}

interface NavRegisterClick{
    fun onNavRegisterClick(v: View)
}

interface UpdateProfileClick{
    fun onUpdateProfileClick(v: View, obj: User)
}

interface NewsContentClick{
    fun onNewsContentClick(v: View)
}

interface NextPageClick{
    fun onNextPageClick(v: View)
}

interface PrevPageClick{
    fun onPrevPageClick(v: View)
}