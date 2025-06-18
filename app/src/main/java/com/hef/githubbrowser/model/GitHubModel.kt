package com.hef.githubbrowser.model

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/16
 */
object GitHubModel {
    private const val BASE_URL = "https://api.github.com/"

    val api: GitHubService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory()) // 添加协程调用适配器
            .build().create(GitHubService::class.java)
    }

//

//    val api: GitHubService by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://api.github.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(CoroutineCallAdapterFactory()) // 添加对 Coroutines 的支持
//            .build()
//            .create(GitHubService::class.java)
//    }
}