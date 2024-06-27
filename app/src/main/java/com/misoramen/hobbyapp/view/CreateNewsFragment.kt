package com.misoramen.hobbyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.misoramen.hobbyapp.R
import com.misoramen.hobbyapp.databinding.FragmentCreateNewsBinding
import com.misoramen.hobbyapp.viewmodel.ContentViewModel
import com.misoramen.hobbyapp.viewmodel.NewsViewModel


class CreateNewsFragment : Fragment() {
    lateinit var binding: FragmentCreateNewsBinding
    private lateinit var newsVM: NewsViewModel
    private lateinit var contentVM: ContentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_news, container, false)
    }
}