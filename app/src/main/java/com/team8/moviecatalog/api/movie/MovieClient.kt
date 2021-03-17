package com.team8.moviecatalog.api.movie

import com.team8.moviecatalog.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MovieClient {

    fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MOVIE_BASE_URL)
//                .client(getInterceptor())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getService() = getRetrofit().create(MovieService::class.java)

}