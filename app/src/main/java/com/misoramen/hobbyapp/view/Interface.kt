package com.misoramen.hobbyapp.view

import android.view.View
import android.widget.CompoundButton
import com.misoramen.hobbyapp.model.HobbyDao

////nama Interface Bebas
//interface TodoCheckedChangeListener {
//    //Nama bebas
//    fun onTodoCheckedChange(cb:CompoundButton, isChecked:Boolean, todo:Todo)
//}

interface ButtonClickListener{
    fun onButtonClick(v:View)
}

interface RadioClickListener{
    fun onRadioClick(v: View)
}

interface DateClickListener{
    fun onDateClick(v:View)
}

interface TimeClickListener{
    fun onTimeClick(v:View)
}