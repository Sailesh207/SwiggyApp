package com.example.swiggyapp.UILayer

import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swiggyapp.DomainLayer.Adapter.MovieAdapter
import com.example.swiggyapp.DomainLayer.ViewModel.MovieViewModel
import com.example.swiggyapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel : MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.movieList
        val adapter = MovieAdapter()
        recyclerView.adapter = adapter

        binding.movieSearch.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.isNotEmpty() == true){
                    viewModel.searchSubject.onNext(newText)
                }
                return true
            }
        })

        viewModel.getMovieDetails.observe(this){ movie ->
            adapter.submitList(movie)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastItemPosition = layoutManager.findLastVisibleItemPosition()

                if(lastItemPosition + 2 >= totalItemCount && !viewModel.isLoading){
                    viewModel.loadMoreMovie()
                }
            }
        })
    }
}