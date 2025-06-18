package com.hef.githubbrowser.model

import com.hef.githubbrowser.ui.MainViewModel

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/18
 */
object GitHubUtils {

    fun getQueryKey(keyword: String?, language: String?): String {
        return when {
            keyword.isNullOrEmpty() && language.isNullOrEmpty() -> MainViewModel.DEFAULT_QUERY_KEY
            keyword.isNullOrEmpty() -> "language:$language"
            language.isNullOrEmpty() -> keyword
            else -> "${keyword} language:$language"  // 把+号改为空格更符合URL规范
        }
    }
}