package com.misoramen.hobbyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.misoramen.hobbyapp.R
import com.misoramen.hobbyapp.databinding.FragmentCreateNewsBinding
import com.misoramen.hobbyapp.model.News
import com.misoramen.hobbyapp.model.User
import com.misoramen.hobbyapp.viewmodel.ContentViewModel
import com.misoramen.hobbyapp.viewmodel.NewsViewModel
import com.misoramen.hobbyapp.viewmodel.UserViewModel
import java.time.LocalDateTime


class CreateNewsFragment : Fragment() {
    lateinit var binding: FragmentCreateNewsBinding
    private lateinit var newsVM: NewsViewModel
    //private lateinit var contentVM: ContentViewModel
    private lateinit var userVM: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateNewsBinding.inflate(inflater, container, true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsVM = ViewModelProvider(this).get(NewsViewModel::class.java)
        userVM = ViewModelProvider(this).get(UserViewModel::class.java)
        //contentVM = ContentViewModel(this).get(ContentViewModel::class.java)

        userVM.getAccount()
        lateinit var curentUser: User

        userVM.usersLD.observe(viewLifecycleOwner, Observer{ user ->
            if (user != null){
                curentUser = user
            }
        })
        binding.btnCreate.setOnClickListener{
            var news = News(binding.txtJudul.toString(), binding.txtDeskripsi.toString(), binding.txtImageURL.toString(), LocalDateTime.now().toString(), curentUser.id, 0)
            val list = listOf(news)


        }
    }
}