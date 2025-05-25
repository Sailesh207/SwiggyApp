package com.example.swiggyapp.RepositoryLayer.Repository

import android.util.Log
import com.example.swiggyapp.DomainLayer.DataClass.MovieResponse
import com.example.swiggyapp.RepositoryLayer.Network.MovieApiService
import io.reactivex.Observable
import javax.inject.Inject

class MovieRepository @Inject constructor (private val movieApiService : MovieApiService) {

    fun getMovieByTitle(query : String, page : Int) : Observable<MovieResponse>{
        return try{
            movieApiService.getMovies(movieTitle = query, page = page)
        }
        catch (e : Exception){
            Log.e("MovieRepository", e.message.toString())
            Observable.error(e)
        }
    }
}