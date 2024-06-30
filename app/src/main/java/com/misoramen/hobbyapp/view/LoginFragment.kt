package com.misoramen.hobbyapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.misoramen.hobbyapp.databinding.FragmentLoginBinding
import com.misoramen.hobbyapp.model.User
import com.misoramen.hobbyapp.viewmodel.UserViewModel

class LoginFragment : Fragment(), LoginUserClick, NavRegisterClick {
    private lateinit var viewModel: UserViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.user = User("","","","","","")
        binding.loginListener = this
        binding.navRegisListener = this
    }

    override fun onLoginUserClick(v: View) {
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.login(binding.user!!)
        viewModel.usersLD.observe(viewLifecycleOwner, Observer { user ->
            if (user != null){
                viewModel.saveUserId(user.id.toString())
                val action = LoginFragmentDirections.actionHomeFragment()
                Navigation.findNavController(v).navigate(action)
            }
            else{
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onNavRegisterClick(v: View) {
        val action = LoginFragmentDirections.actionRegisterFragment()
        Navigation.findNavController(v).navigate(action)
    }
}