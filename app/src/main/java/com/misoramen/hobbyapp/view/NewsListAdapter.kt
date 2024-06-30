package com.misoramen.hobbyapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.misoramen.hobbyapp.databinding.NewsListItemBinding
import com.misoramen.hobbyapp.model.News
import com.misoramen.hobbyapp.model.NewsWithAuthor
import com.misoramen.hobbyapp.viewmodel.NewsViewModel
import com.squareup.picasso.Picasso

class NewsListAdapter(val newsList:ArrayList<NewsWithAuthor>)
    : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>(), NewsContentClick
{
    class NewsViewHolder(var binding: NewsListItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.binding.news = newsList[position]
        holder.binding.contentListener = this
    }
    fun updateNewsList(newNewsList: ArrayList<NewsWithAuthor>) {
        newsList.clear()
        newsList.addAll(newNewsList)
        notifyDataSetChanged()
    }

    override fun onNewsContentClick(v: View) {
        val idNews = v.tag.toString().toInt()
        if (idNews != null){
            val action = MainFragmentDirections.actionItemHomeToNewsContentFragment(idNews)
            Navigation.findNavController(v).navigate(action)
        }
    }

}