package com.example.swiggyapp.DomainLayer.ViewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swiggyapp.DomainLayer.DataClass.MovieData
import com.example.swiggyapp.DomainLayer.DataClass.MovieResponse
import com.example.swiggyapp.RepositoryLayer.Repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {
    private val movieList : MutableLiveData<List<MovieData>> = MutableLiveData()
    val getMovieDetails : LiveData<List<MovieData>> = movieList
    val searchSubject = PublishSubject.create<String>()
    private var disposable: Disposable
    var isLoading = false
    var currentQuery = ""
    var currentPage = 1
    var totalPages = 1

    init {

        disposable = searchSubject
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS)
            .switchMap { query ->
                currentQuery = query
                currentPage = 1
                isLoading = true
                movieRepository.getMovieByTitle(currentQuery, currentPage)
                    .onErrorReturn { e ->
                        isLoading = false
                        MovieResponse(emptyList(), "", "", "")
                    }
                    .doOnError{ e ->
                        Log.e("MovieViewModel", e.message.toString())
                        isLoading = false
                    }
                    .doOnTerminate {
                        Log.e("MovieViewModel", "Api Request Failed")
                        isLoading = false
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
            .subscribe { response ->
                if (response.response == "True") {
                    totalPages = (response.totalResults.toIntOrNull() ?: 0) / 10 + 1
                    movieList.value = response.movieList
                }
            }
    }

    @SuppressLint("CheckResult")
    fun fetchMovies(query : String, page : Int){
        if(isLoading) return

        isLoading = true
        movieRepository.getMovieByTitle(query, page = page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn { e ->
                isLoading = false
                MovieResponse(emptyList(), "", "", "")
            }
            .doOnError{ e ->
                Log.e("MovieViewModel", e.message.toString())
                isLoading = false
            }
            .doOnTerminate {
                Log.e("MovieViewModel", "Api Request Failed")
                isLoading = false
            }
            .subscribe{ response ->
                if(response.response == "True"){
                    totalPages = (response.totalResults.toIntOrNull() ?: 0) / 10 + 1
                    if(page == 1){
                        movieList.value = response.movieList
                    }
                    else{
                        movieList.value = movieList.value.orEmpty() + response.movieList
                    }
                }
            }
    }

    fun loadMoreMovie(){
        if(!isLoading && currentPage < totalPages){
            currentPage++
            fetchMovies(currentQuery, currentPage)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}