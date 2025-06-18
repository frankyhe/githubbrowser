package com.hef.githubbrowser.model

import com.hef.githubbrowser.model.bean.SearchRepositoriesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/16
 */

interface GitHubService {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "desc",  // 可选参数，根据需求添加
        @Query("order") order: String = "stars", // 可选参数，根据需求添加
        @Query("page") page: Int?, // 可选参数，根据需求添加
        @Query("per_page") per_page: Int = 20 // 可选参数，根据需求添加
    ): SearchRepositoriesResponse?
}