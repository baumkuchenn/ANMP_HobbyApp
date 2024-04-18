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

class NewsContentFragment : Fragment() {
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

        val idNews = NewsContentFragmentArgs.fromBundle(requireArguments()).idNews.toString()
        newsVM = ViewModelProvider(this).get(NewsViewModel::class.java)
        contentVM = ViewModelProvider(this).get(ContentViewModel::class.java)

        newsVM.getCertainNews(idNews)
        contentVM.loadContent(idNews, currentContentIndex.toString())

        observeViewModel()

        binding.btnNextContent.setOnClickListener {
            if (currentContentIndex < contentVM.countLD.value!!.toInt() - 1) {
                currentContentIndex++
                val idNews = NewsContentFragmentArgs.fromBundle(requireArguments()).idNews.toString()
                val index = currentContentIndex.toString()
                contentVM.loadContent(idNews, index)
                contentVM.contentLD.observe(viewLifecycleOwner, Observer{content ->
                    binding.txtIsiContent.setText(content[0].isi)
                })
            }
        }

        binding.btnPrevContent.setOnClickListener {
            if (currentContentIndex > 0) {
                currentContentIndex--
                val idNews = NewsContentFragmentArgs.fromBundle(requireArguments()).idNews.toString()
                val index = currentContentIndex.toString()
                contentVM.loadContent(idNews, index)
                contentVM.contentLD.observe(viewLifecycleOwner, Observer{content ->
                    binding.txtIsiContent.setText(content[0].isi)
                })
            }
        }
    }

    fun observeViewModel() {
        newsVM.newsLD.observe(viewLifecycleOwner, Observer { news ->
            val picasso = Picasso.Builder(context)
            picasso.listener { picasso, uri, exception ->
                exception.printStackTrace() }
            picasso.build().load(news[0].imageUrl).into(binding.imgPhotoContent)
            binding.txtGenreContent.setText(news[0].genre)
            binding.txtJudulContent.setText(news[0].judul)
            binding.txtAuthorContent.setText("@" + news[0].author)
        })
        contentVM.contentLD.observe(viewLifecycleOwner, Observer{content ->
            binding.txtIsiContent.setText(content[0].isi)
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

}