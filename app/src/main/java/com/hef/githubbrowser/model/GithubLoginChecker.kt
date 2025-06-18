package com.hef.githubbrowser.model

import android.util.Log
import android.webkit.CookieManager

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/17
 */
object GithubLoginChecker {
    const val TAG = "GithubLoginChecker"

    const val LOGIN_URL = "https://github.com/login"
    const val LOGOUT_URL = "https://github.com/logout"

    const val LOGIN_TAG = "logged_in=yes"
    const val LOGOUT_TAG = "logged_in=no"

    init {
        CookieManager.getInstance().apply {
            this.setAcceptCookie(true)
        }
    }

    fun isLogin(): Boolean{
        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(LOGIN_URL)
        return isLogin(cookies)
    }

    fun isLogin(cookies : String?): Boolean{
        Log.d(TAG, "Cookies for $LOGIN_URL: $cookies")
        return cookies?.contains(LOGIN_TAG) ?: false
    }
}