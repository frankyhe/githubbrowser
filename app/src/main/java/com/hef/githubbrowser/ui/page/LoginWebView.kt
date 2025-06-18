package com.hef.githubbrowser.ui.page

import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.hef.githubbrowser.model.GithubLoginChecker

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/17
 */

@Composable
fun LoginWebView(url: String, onLogin: (login: Boolean)-> Unit) {
    AndroidView(factory = { ctx ->
        WebView(ctx).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    val cookieManager = CookieManager.getInstance()
                    val cookies = cookieManager.getCookie(url)
                    Log.d("WEBVIEW_COOKIE", "Cookies for $url: $cookies")
                    if(GithubLoginChecker.isLogin(cookies)){
                        onLogin(true)
                    }
                }
            }

            CookieManager.getInstance().let {
                it.setAcceptCookie(true)
                it.setAcceptThirdPartyCookies(this, true)
            }
            loadUrl(url)
        }
    })
}