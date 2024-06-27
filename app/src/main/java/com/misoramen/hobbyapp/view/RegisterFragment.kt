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
import com.misoramen.hobbyapp.R
import com.misoramen.hobbyapp.databinding.FragmentRegisterBinding
import com.misoramen.hobbyapp.model.User
import com.misoramen.hobbyapp.viewmodel.UserViewModel

class RegisterFragment : Fragment(), ButtonClickListener {
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
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Initialize binding.user with a User object
        binding.user = User("", "", "", "", "", "")

        binding.regislistener = this
        binding.loginlistener = this
    }

    override fun onButtonClick(v: View) {
        when (v.id) {
            R.id.btnCreateRegister -> {
                val password = binding.txtPasswordRegister.text.toString()
                val confirmPass = binding.txtConfirmPassRegister.text.toString()

                if (password.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                    return
                }

                if (password == confirmPass) {

                    if (binding.user != null) {
                        viewModel.createAccount(binding.user!!)
                        viewModel.messageLD.observe(viewLifecycleOwner, Observer { message ->
                            if (viewModel.resultLD.value == "success") {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                val action = RegisterFragmentDirections.actionLoginFragment()
                                Navigation.findNavController(v).navigate(action)
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else {
                        Toast.makeText(context, "User data is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btnSignInRegister -> {
                val action = RegisterFragmentDirections.actionLoginFragment()
                Navigation.findNavController(v).navigate(action)
            }
        }
    }

}
