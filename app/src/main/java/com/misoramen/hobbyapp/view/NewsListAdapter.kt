package com.misoramen.hobbyapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.misoramen.hobbyapp.R
import com.misoramen.hobbyapp.databinding.NewsListItemBinding
import com.misoramen.hobbyapp.model.News
import com.misoramen.hobbyapp.model.NewsWithAuthor
import com.misoramen.hobbyapp.viewmodel.NewsViewModel
import com.squareup.picasso.Picasso

class NewsListAdapter(val newsList:ArrayList<NewsWithAuthor>)
    : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() ,ButtonClickListener
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
//        val picasso = Picasso.Builder(holder.itemView.context)
//        picasso.listener { picasso, uri, exception ->
//            exception.printStackTrace() }
//        picasso.build().load(newsList[position].imageUrl).into(holder.binding.imgPhotoNews)
//        holder.binding.txtGenreNews.setText(newsList[position].genre)
//        holder.binding.txtJudulNews.setText(newsList[position].judul)
//        holder.binding.txtUserNews.setText("@" + newsList[position].author)
//        holder.binding.txtDescriptionNews.setText(newsList[position].description)
//
//        holder.binding.btnReadNews.setOnClickListener {
//
//        }
        holder.binding.news=newsList[position]
        holder.binding.readlistener=this
    }

    fun updateNewsList(newNewsList: ArrayList<NewsWithAuthor>) {
        newsList.clear()
        newsList.addAll(newNewsList)
        notifyDataSetChanged()
    }

    override fun onButtonClick(v: View) {
        when (v.id) {
            R.id.btnReadNews -> {
                val idNews = v.tag.toString().toInt()
                if (idNews != null){
                    val action = MainFragmentDirections.actionItemHomeToNewsContentFragment(idNews)
                    Navigation.findNavController(v).navigate(action)
                }
            }
        }
    }

}