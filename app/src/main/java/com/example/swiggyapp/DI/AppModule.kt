package com.example.swiggyapp.DI

import com.example.swiggyapp.RepositoryLayer.Network.MovieApiService
import com.example.swiggyapp.RepositoryLayer.Repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Provides
    @Singleton
    fun provideMovieRepository(movieApiService: MovieApiService) : MovieRepository {
        return MovieRepository(movieApiService)
    }

    @Provides
    @Singleton
    fun provideMovieApiService() : MovieApiService {
        return Retrofit.Builder()
            .baseUrl(MovieApiService.base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}