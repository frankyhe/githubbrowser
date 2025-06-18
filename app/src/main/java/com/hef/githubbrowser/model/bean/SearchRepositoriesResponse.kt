package com.hef.githubbrowser.model.bean

import com.google.gson.annotations.SerializedName

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/16
 */

data class SearchRepositoriesResponse(
    @SerializedName("total_count")  var totalCount: Int,
    @SerializedName("incomplete_results") var incompleteResults: Boolean,
    @SerializedName("items") var items: List<Repository>? = null
)