package com.hef.githubbrowser.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hef.githubbrowser.model.GitHubModel
import com.hef.githubbrowser.model.GitHubUtils
import com.hef.githubbrowser.model.bean.Repository
import kotlinx.coroutines.CoroutineExceptionHandler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/16
 */
class MainViewModel : ViewModel {
    companion object {
        const val TAG = "MainViewModel"

        const val DEFAULT_QUERY_KEY = "language:kotlin"
    }

    var totalCountLiveData = MutableLiveData<Int>()
    var repositoriesLiveData = MutableLiveData<List<Repository>>()

    var loadingLiveData = MutableLiveData(false)

    var currentQueryKey: String = DEFAULT_QUERY_KEY

    private var repositories: List<Repository> = emptyList<Repository>()

    constructor() {

    }

    fun searchRepositories(keyword : String, language: String, page: Int){
        Log.d(TAG, "searchRepositories(): keyword = $keyword, language = $language, page = $page")
        currentQueryKey = GitHubUtils.getQueryKey(keyword, language)
        Log.d(TAG, "searchRepositories(): currentQueryKey = $currentQueryKey")
        loadSearchRepositories(currentQueryKey, page)
        loadingLiveData.postValue(true)
    }

    fun searchRepositories(page: Int){
        Log.d(TAG, "searchRepositories(): currentQueryKey = $currentQueryKey, page = $page")
        loadSearchRepositories(currentQueryKey, page)
    }

    fun clearRepositories(){
        repositories = emptyList()
        repositoriesLiveData.postValue(repositories)
        totalCountLiveData.postValue(0)
    }

    // 1. 创建异常处理器
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "CoroutineException: ${throwable.message}")
        // 可在此处进行错误上报或UI状态更新
    }

    private fun loadSearchRepositories(query: String, page: Int) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            var response = GitHubModel.api.searchRepositories(query = query, page = page)
            Log.d(TAG, "response = " + response.toString());

            totalCountLiveData.postValue(response?.totalCount)

            response?.items?.let {
                repositories += it
                repositoriesLiveData.postValue(repositories)
            }

            if(isAllLoaded(response?.totalCount ?: 0)){
                loadingLiveData.postValue(false)
            }
        }
    }

    fun isAllLoaded(totalCount: Int): Boolean{
        return repositories.size == totalCount
    }

    fun isAllLoaded(): Boolean{
        return repositories.size == totalCountLiveData.value
    }
}