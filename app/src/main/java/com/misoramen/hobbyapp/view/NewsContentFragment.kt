package com.misoramen.hobbyapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.misoramen.hobbyapp.databinding.FragmentNewsContentBinding
import com.misoramen.hobbyapp.model.News
import com.misoramen.hobbyapp.viewmodel.ContentViewModel
import com.misoramen.hobbyapp.viewmodel.NewsViewModel
import com.misoramen.hobbyapp.viewmodel.UserViewModel
import com.squareup.picasso.Picasso

class NewsContentFragment : Fragment(), NextPageClick, PrevPageClick {
    private lateinit var binding: FragmentNewsContentBinding
    private lateinit var contentVM: ContentViewModel
    private lateinit var newsVM: NewsViewModel
    private var currentContentIndex = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idNews = NewsContentFragmentArgs.fromBundle(requireArguments()).idNews
        newsVM = ViewModelProvider(this).get(NewsViewModel::class.java)
        contentVM = ViewModelProvider(this).get(ContentViewModel::class.java)

        newsVM.getCertainNews(idNews)
        contentVM.loadContent(idNews, currentContentIndex.toString().toInt())

        observeViewModel()

        binding.nextListener = this
        binding.prevListener = this
    }

    fun observeViewModel() {
        newsVM.newsLD.observe(viewLifecycleOwner, Observer { news ->
            Log.d("content", news[0].toString())
            binding.news = news[0]
        })
        contentVM.contentLD.observe(viewLifecycleOwner, Observer{content ->
            Log.d("content", content[0].toString())
            binding.content = content[0]
            contentVM.countLD.observe(viewLifecycleOwner) { count ->
                binding.page = "${currentContentIndex + 1} of $count"
            }
        })
        contentVM.contentLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.txtErrorContent?.visibility = View.VISIBLE
            } else {
                binding.txtErrorContent?.visibility = View.GONE
            }
        })
        contentVM.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.cardViewContent.visibility = View.GONE
                binding.progressLoadContent.visibility = View.VISIBLE
            } else {
                binding.cardViewContent.visibility = View.VISIBLE
                binding.progressLoadContent.visibility = View.GONE
            }
        })
    }

    override fun onNextPageClick(v: View) {
        if (currentContentIndex < contentVM.countLD.value!!.toInt() - 1) {
            currentContentIndex++
            val idNews = NewsContentFragmentArgs.fromBundle(requireArguments()).idNews
            val index = currentContentIndex.toString().toInt()
            contentVM.loadContent(idNews, index)
            contentVM.contentLD.observe(viewLifecycleOwner, Observer{content ->
                binding.content = content[0]
                contentVM.countLD.observe(viewLifecycleOwner) { count ->
                    binding.page = "${currentContentIndex + 1} of $count"
                }
            })
        }
    }

    override fun onPrevPageClick(v: View) {
        if (currentContentIndex > 0) {
            currentContentIndex--
            val idNews = NewsContentFragmentArgs.fromBundle(requireArguments()).idNews
            val index = currentContentIndex.toString().toInt()
            contentVM.loadContent(idNews, index)
            contentVM.contentLD.observe(viewLifecycleOwner, Observer{content ->
                binding.content = content[0]
                contentVM.countLD.observe(viewLifecycleOwner) { count ->
                    binding.page = "${currentContentIndex + 1} of $count"
                }
            })
        }
    }

}