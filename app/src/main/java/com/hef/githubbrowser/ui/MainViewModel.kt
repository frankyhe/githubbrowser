package com.hef.githubbrowser.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hef.githubbrowser.model.GitHubModel
import com.hef.githubbrowser.model.GitHubUtils
import com.hef.githubbrowser.model.bean.Repository
import kotlinx.coroutines.CoroutineExceptionHandler

import kotlinx.coroutines.launch

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/16
 */
class MainViewModel : ViewModel() {
    companion object {
        const val TAG = "MainViewModel"

        const val DEFAULT_QUERY_KEY = "language:kotlin"
    }

    var totalCountLiveData = MutableLiveData<Int>()
    var repositoriesLiveData = MutableLiveData<List<Repository>>()

    var loadingLiveData = MutableLiveData(false)

    var currentQueryKey: String = DEFAULT_QUERY_KEY

    private var repositories: List<Repository> = emptyList<Repository>()

    var context: Context? = null

    fun setActivity(activity: Activity) {
        context = activity
    }

    fun searchRepositories(keyword: String, language: String, page: Int) {
        Log.d(TAG, "searchRepositories(): keyword = $keyword, language = $language, page = $page")
        currentQueryKey = GitHubUtils.getQueryKey(keyword, language)
        Log.d(TAG, "searchRepositories(): currentQueryKey = $currentQueryKey")
        loadSearchRepositories(currentQueryKey, page)
        loadingLiveData.postValue(true)
    }

    fun searchRepositories(page: Int) {
        Log.d(TAG, "searchRepositories(): currentQueryKey = $currentQueryKey, page = $page")
        loadSearchRepositories(currentQueryKey, page)
    }

    fun clearRepositories() {
        repositories = emptyList()
        repositoriesLiveData.postValue(repositories)
        totalCountLiveData.postValue(0)
    }

    // 1. 创建异常处理器
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "CoroutineException: ${throwable.message}")
        // 可在此处进行错误上报或UI状态更新

        context?.let {
            Toast.makeText(it, "${throwable.message}", Toast.LENGTH_SHORT).show()
        }

        //发生异常，通知数据加载结束
        loadingLiveData.postValue(false)
    }

    private fun loadSearchRepositories(query: String, page: Int) {
        viewModelScope.launch(exceptionHandler) {
            var response = GitHubModel.api.searchRepositories(query = query, page = page)
            Log.d(TAG, "response = " + response.toString());

            totalCountLiveData.postValue(response?.totalCount)

            response?.items?.let {
                repositories += it
                repositoriesLiveData.postValue(repositories)
            }

            //没有检索到结果, 弹框提示
            if (response == null || response.totalCount == 0) {
                context?.let {
                    Toast.makeText(it, "Pull completed, no data available！", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            if (isAllLoaded(response?.totalCount ?: 0)) {
                loadingLiveData.postValue(false)
            }
        }
    }

    private fun isAllLoaded(totalCount: Int): Boolean {
        return repositories.size == totalCount
    }
}