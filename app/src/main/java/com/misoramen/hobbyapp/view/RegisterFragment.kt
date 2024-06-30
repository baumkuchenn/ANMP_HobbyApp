package com.misoramen.hobbyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.misoramen.hobbyapp.databinding.FragmentRegisterBinding
import com.misoramen.hobbyapp.model.User
import com.misoramen.hobbyapp.viewmodel.UserViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RegisterFragment : Fragment(), RegisterUserClick, NavLoginClick {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.user = User("", "", "", "", "", "")
        binding.registerListener = this
        binding.navLoginListener = this
    }

    override fun onRegisterUserClick(v: View) {
        val currentInstant = Instant.now()
        val currentZone = ZoneId.systemDefault()
        val localDateTime = currentInstant.atZone(currentZone).toLocalDateTime()
        val formattedDateTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        if (binding.user!!.password.isNotEmpty() && binding.user!!.password == binding.confirmPass){
            binding.user!!.createdAt = formattedDateTime
            viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
            viewModel.createAccount(binding.user!!)
            viewModel.messageLD.observe(viewLifecycleOwner, Observer { message ->
                if (viewModel.resultLD.value == "success"){
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    onNavLoginClick(v)
                }
                else{
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        else{
            Toast.makeText(context, "Password is empty or not same", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNavLoginClick(v: View) {
        val action = RegisterFragmentDirections.actionLoginFragment()
        Navigation.findNavController(v).navigate(action)
    }
}