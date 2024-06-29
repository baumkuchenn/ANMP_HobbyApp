package com.misoramen.hobbyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.misoramen.hobbyapp.R
import com.misoramen.hobbyapp.databinding.FragmentNewsContentBinding
import com.misoramen.hobbyapp.viewmodel.ContentViewModel
import com.misoramen.hobbyapp.viewmodel.NewsViewModel

class NewsContentFragment : Fragment(), ButtonClickListener {
    private lateinit var binding: FragmentNewsContentBinding
    private lateinit var contentVM: ContentViewModel
    private lateinit var newsVM: NewsViewModel
    private var currentContentIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve idNews from arguments using NavArgs
        val idNews = NewsContentFragmentArgs.fromBundle(requireArguments()).idNews.toInt()

        // Initialize ViewModel instances
        newsVM = ViewModelProvider(this).get(NewsViewModel::class.java)
        contentVM = ViewModelProvider(this).get(ContentViewModel::class.java)

        // Load specific news and content based on idNews
        newsVM.getCertainNews(idNews)
        contentVM.loadContent(idNews, currentContentIndex)

        // Observe LiveData from ViewModels
        observeViewModel()

        // Set button click listeners
        binding.nextlistener = this
        binding.prevlistener = this
    }

    private fun observeViewModel() {
        // Observe newsLD from NewsViewModel
        newsVM.newsLD.observe(viewLifecycleOwner, Observer { news ->
            binding.news = news[0]
        })

        // Observe contentLD, countLD, contentLoadErrorLD, and loadingLD from ContentViewModel
        contentVM.contentLD.observe(viewLifecycleOwner, Observer { content ->
            binding.content = content[0]
            contentVM.countLD.observe(viewLifecycleOwner) { count ->
                binding.txtPageContent.text = "${currentContentIndex + 1} of $count"
            }
        })

        contentVM.contentLoadErrorLD.observe(viewLifecycleOwner, Observer { isError ->
            binding.txtErrorContent?.visibility = if (isError) View.VISIBLE else View.GONE
        })

        contentVM.loadingLD.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.cardViewContent.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.progressLoadContent.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    override fun onButtonClick(v: View) {
        // Retrieve idNews from arguments using NavArgs
        val idNews = NewsContentFragmentArgs.fromBundle(requireArguments()).idNews.toInt()

        // Handle button clicks
        when (v.id) {
            R.id.btnNextContent -> {
                if (currentContentIndex < contentVM.countLD.value!!.toInt() - 1) {
                    currentContentIndex++
                    val idNews =
                        NewsContentFragmentArgs.fromBundle(requireArguments()).idNews.toString()
                            .toInt()
                    val index = currentContentIndex.toString().toInt()
                    contentVM.loadContent(idNews, index)
                    contentVM.contentLD.observe(viewLifecycleOwner, Observer { content ->
                        binding.txtIsiContent.setText(content[0].isi)
                        contentVM.countLD.observe(viewLifecycleOwner) { count ->
                            binding.txtPageContent.text = "${currentContentIndex + 1} of $count"
                        }
                    })
                }
            }

            R.id.btnPrevContent -> {
                if (currentContentIndex > 0) {
                    currentContentIndex--
                    val idNews =
                        NewsContentFragmentArgs.fromBundle(requireArguments()).idNews.toString()
                            .toInt()
                    val index = currentContentIndex.toString().toInt()
                    contentVM.loadContent(idNews, index)
                    contentVM.contentLD.observe(viewLifecycleOwner, Observer { content ->
                        binding.txtIsiContent.setText(content[0].isi)
                        contentVM.countLD.observe(viewLifecycleOwner) { count ->
                            binding.txtPageContent.text = "${currentContentIndex + 1} of $count"
                        }
                    })
                }
            }
        }
    }
}
