package com.hef.githubbrowser.ui.page

import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/17
 */
@Composable
fun BaseWebView(url: String) {
    Log.d("BaseWebView", "url = $url")
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true // 启用 JavaScript
                webViewClient = WebViewClient() // 处理页面导航
                loadUrl(url)
            }
        }
    )
}
