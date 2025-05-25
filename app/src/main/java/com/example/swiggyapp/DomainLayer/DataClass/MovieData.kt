package com.example.swiggyapp.DomainLayer.DataClass

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Search") val movieList : List<MovieData>,
    @SerializedName("totalResults") val totalResults : String,
    @SerializedName("Response") val response : String,
    @SerializedName("Error") val error : String
)

data class MovieData(
    @SerializedName("Title") val movieTitle : String,
    @SerializedName("Poster") val moviePoster : String,
    @SerializedName("imdbID") val id : String,
    @SerializedName("Year") val year : String
)