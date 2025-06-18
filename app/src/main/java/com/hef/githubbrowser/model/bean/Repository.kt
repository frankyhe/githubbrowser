package com.hef.githubbrowser.model.bean

import com.google.gson.annotations.SerializedName

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/16
 */
data class Repository(
    val id: Int,
    val name: String?,
    @SerializedName("full_name")val fullName: String?,
    val description: String?,
    val url: String?,
    val language: String?,
    @SerializedName("stargazers_count")val stargazers : Int,
    @SerializedName("html_url")val html: String?
)
