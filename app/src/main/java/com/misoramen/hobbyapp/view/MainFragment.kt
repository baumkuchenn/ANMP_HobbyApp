package com.misoramen.hobbyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.misoramen.hobbyapp.R
import com.misoramen.hobbyapp.databinding.FragmentMainBinding
import com.misoramen.hobbyapp.viewmodel.NewsViewModel

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: NewsViewModel
    private val newsListAdapter = NewsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        viewModel.loadNews()

        binding.recView.layoutManager = LinearLayoutManager(context)
        binding.recView.adapter = newsListAdapter

        observeViewModel()

        binding.refreshLayoutHome.setOnRefreshListener {
            binding.recView.visibility = View.GONE
            binding.txtErrorHome.visibility = View.GONE
            binding.progressLoadHome.visibility = View.VISIBLE
            viewModel.loadNews()
            binding.refreshLayoutHome.isRefreshing = false
        }
    }

    fun observeViewModel() {
        viewModel.newsLD.observe(viewLifecycleOwner, Observer {
            newsListAdapter.updateNewsList(it)
        })
        viewModel.newsLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.txtErrorHome?.visibility = View.VISIBLE
            } else {
                binding.txtErrorHome?.visibility = View.GONE
            }
        })
        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.recView.visibility = View.GONE
                binding.progressLoadHome.visibility = View.VISIBLE
            } else {
                binding.recView.visibility = View.VISIBLE
                binding.progressLoadHome.visibility = View.GONE
            }
        })
    }
}