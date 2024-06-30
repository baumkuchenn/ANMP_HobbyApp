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
import com.misoramen.hobbyapp.R
import com.misoramen.hobbyapp.databinding.FragmentProfileBinding
import com.misoramen.hobbyapp.viewmodel.UserViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val id = viewModel.loadUserId()
        viewModel.getAccount(id)

        observeViewModel()

        binding.refreshLayoutProfile.setOnRefreshListener {
            binding.txtErrorProfile.visibility = View.GONE
            binding.progressLoadProfile.visibility = View.VISIBLE
            viewModel.getAccount(id)
            binding.refreshLayoutProfile.isRefreshing = false
        }

        binding.btnChangeProfile.setOnClickListener {
            val firstName = binding.txtFirstNameProfile.text.toString()
            val lastName = binding.txtLastNameProfile.text.toString()
            val newPass = binding.txtPasswordProfile.text.toString()
            val newConfirmPass = binding.txtConfirmPassProfile.text.toString()

            if (newPass.isNotEmpty() && newPass == newConfirmPass){
                viewModel.updateProfile(id, firstName, lastName, newPass)
                viewModel.messageLD.observe(viewLifecycleOwner, Observer { message ->
                    if (viewModel.resultLD.value == "success"){
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

        binding.btnSignOutProfile.setOnClickListener {
            val action = ProfileFragmentDirections.actionItemProfileToLoginFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun observeViewModel() {
        viewModel.usersLD.observe(viewLifecycleOwner, Observer { user ->
            if (user != null){
                binding.txtFirstNameProfile.setText(user.firstName)
                binding.txtLastNameProfile.setText(user.lastName)
            }
        })
        viewModel.userLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.txtErrorProfile?.visibility = View.VISIBLE
            } else {
                binding.txtErrorProfile?.visibility = View.GONE
            }
        })
        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.constraintLayoutProfile.visibility = View.GONE
                binding.progressLoadProfile.visibility = View.VISIBLE
            } else {
                binding.constraintLayoutProfile.visibility = View.VISIBLE
                binding.progressLoadProfile.visibility = View.GONE
            }
        })
    }
}