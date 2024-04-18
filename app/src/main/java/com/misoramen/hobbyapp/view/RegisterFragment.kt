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
import com.misoramen.hobbyapp.viewmodel.UserViewModel

class RegisterFragment : Fragment() {
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
        binding.btnCreateRegister.setOnClickListener {
            //Cek username di database, cek password dan confirm pass, simpan akun ke db
            var username = binding.txtUsernameRegister.text.toString()
            var email = binding.txtEmailRegister.text.toString()
            var firstName = binding.txtFirstNameRegister.text.toString()
            var lastName = binding.txtLastNameRegister.text.toString()
            var password = binding.txtPasswordRegister.text.toString()
            var confirmPass = binding.txtConfirmPassRegister.text.toString()

            if (password == confirmPass){
                viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
                viewModel.createAccount(username, firstName, lastName, email, password)
                viewModel.messageLD.observe(viewLifecycleOwner, Observer { message ->
                    if (viewModel.resultLD.value == "success"){
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        val action = RegisterFragmentDirections.actionLoginFragment()
                        Navigation.findNavController(it).navigate(action)
                    }
                    else{
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else{
                Toast.makeText(context, "Password is not same", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnSignInRegister.setOnClickListener {
            val action = RegisterFragmentDirections.actionLoginFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
}