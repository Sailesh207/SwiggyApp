package com.example.swiggyapp.DomainLayer.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swiggyapp.DomainLayer.DataClass.MovieData
import com.example.swiggyapp.R

class MovieAdapter : ListAdapter<MovieData, MovieAdapter.MovieViewHolder>(MovieDiffUtil()) {

    inner class MovieViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val imageView : ImageView = view.findViewById(R.id.movie_image)
        val movieTitle : TextView = view.findViewById(R.id.movie_title)
        val movieRelease : TextView = view.findViewById(R.id.movie_release)
        fun bind(movie : MovieData){
            Glide.with(imageView.context)
                .load(movie.moviePoster)
                .into(imageView)
            movieTitle.text = movie.movieTitle
            movieRelease.text = movie.year
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item ,parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieDiffUtil : DiffUtil.ItemCallback<MovieData>(){
        override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
            return oldItem == newItem
        }
    }
}