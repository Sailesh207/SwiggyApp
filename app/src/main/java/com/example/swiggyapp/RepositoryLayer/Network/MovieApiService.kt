package com.example.swiggyapp.RepositoryLayer.Network

import com.example.swiggyapp.DomainLayer.DataClass.MovieResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("?")
    fun getMovies(
        @Query("apikey") apikey : String = "1c9aafa5",
        @Query("s") movieTitle : String,
        @Query("type") type : String = "movie",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ) : Observable<MovieResponse>

    companion object{
        const val base_url = "https://www.omdbapi.com/"
    }
}