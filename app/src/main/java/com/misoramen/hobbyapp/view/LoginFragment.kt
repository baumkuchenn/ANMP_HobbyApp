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
import com.misoramen.hobbyapp.databinding.FragmentLoginBinding
import com.misoramen.hobbyapp.model.User
import com.misoramen.hobbyapp.viewmodel.UserViewModel

class LoginFragment : Fragment(), ButtonClickListener {
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
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.user = User("", "", "", "", "", "") // Initialize user with empty fields
        binding.loginlistener = this
        binding.regislistener = this
    }

    override fun onButtonClick(v: View) {
        when (v.id) {
            R.id.btnSignInLogin -> {
                val user = binding.user
                if (user != null) {
                    viewModel.login(user)
                    viewModel.usersLD.observe(viewLifecycleOwner, Observer { loggedInUser ->
                        if (loggedInUser != null) {
                            // Navigate to home fragment upon successful login
                            val action = LoginFragmentDirections.actionHomeFragment()
                            Navigation.findNavController(v).navigate(action)
                        } else {
                            // Show login failed message
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    // Handle case when user is null (if needed)
                    Toast.makeText(context, "Username or password is empty", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btnCreateLogin -> {
                // Navigate to register fragment
                val action = LoginFragmentDirections.actionRegisterFragment()
                Navigation.findNavController(v).navigate(action)
            }
        }
    }
}
