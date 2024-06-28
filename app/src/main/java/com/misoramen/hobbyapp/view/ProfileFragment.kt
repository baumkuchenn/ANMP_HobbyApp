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
import com.misoramen.hobbyapp.databinding.FragmentProfileBinding
import com.misoramen.hobbyapp.viewmodel.UserViewModel

class ProfileFragment : Fragment(), ButtonClickListener {
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
        binding.signoutlistener = this
        binding.changelistener = this

        viewModel.getAccount()

        observeViewModel()

        binding.refreshLayoutProfile.setOnRefreshListener {
            binding.txtErrorProfile.visibility = View.GONE
            binding.progressLoadProfile.visibility = View.VISIBLE
            viewModel.getAccount()
            binding.refreshLayoutProfile.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.usersLD.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                binding.user = it
            }
        })

        viewModel.userLoadErrorLD.observe(viewLifecycleOwner, Observer {
            binding.txtErrorProfile.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.constraintLayoutProfile.visibility = View.GONE
                binding.progressLoadProfile.visibility = View.VISIBLE
            } else {
                binding.constraintLayoutProfile.visibility = View.VISIBLE
                binding.progressLoadProfile.visibility = View.GONE
            }
        })
    }

    override fun onButtonClick(v: View) {
        when (v.id) {
            R.id.btnChangeProfile -> {
                val newPass = binding.txtPasswordProfile.text.toString()
                val newConfirmPass = binding.txtConfirmPassProfile.text.toString()

               if (newPass.isNotEmpty() && newConfirmPass.isNotEmpty()){
                   if (newPass == newConfirmPass) {
                       // Set updated values to the user object
                       val updatedUser = binding.user!!.copy(
                           firstName = binding.txtFirstNameProfile.text.toString(),
                           lastName = binding.txtLastNameProfile.text.toString(),
                           password = newPass
                       )
                       viewModel.updateProfile(updatedUser)
                       viewModel.messageLD.observe(viewLifecycleOwner, Observer { message ->
                           Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                       })
                   } else {
                       Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                   }
               }else{
                   Toast.makeText(context, "Passwords is empty", Toast.LENGTH_SHORT).show()
               }
            }
            R.id.btnSignOutProfile -> {
                val action = ProfileFragmentDirections.actionItemProfileToLoginFragment()
                Navigation.findNavController(v).navigate(action)
            }
        }
    }

}
