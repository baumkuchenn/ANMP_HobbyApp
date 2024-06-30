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
import com.misoramen.hobbyapp.model.User
import com.misoramen.hobbyapp.viewmodel.UserViewModel

class ProfileFragment : Fragment(), NavLoginClick, UpdateProfileClick {
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

        binding.updateListener = this
        binding.logOutListener = this

        binding.refreshLayoutProfile.setOnRefreshListener {
            binding.txtErrorProfile.visibility = View.GONE
            binding.progressLoadProfile.visibility = View.VISIBLE
            viewModel.getAccount(id)
            binding.refreshLayoutProfile.isRefreshing = false
        }
    }

    fun observeViewModel() {
        viewModel.usersLD.observe(viewLifecycleOwner, Observer { user ->
            binding.user = user
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

    override fun onNavLoginClick(v: View) {
        val action = ProfileFragmentDirections.actionItemProfileToLoginFragment()
        Navigation.findNavController(v).navigate(action)
    }

    override fun onUpdateProfileClick(v: View, obj: User) {
        if (binding.newPass?.isNotEmpty() == true && binding.newPass == binding.newConfirmPass){
            val id = viewModel.loadUserId()
            obj.password = binding.newPass!!
            obj.id = id!!.toInt()
            viewModel.updateProfile(obj)
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
}