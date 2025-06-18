package com.hef.githubbrowser

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hef.githubbrowser.ui.theme.GithubbrowserTheme

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hef.githubbrowser.ui.MainViewModel
import com.hef.githubbrowser.ui.page.BaseWebView
import com.hef.githubbrowser.ui.page.SearchDialog

class TestActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private val vm: MainViewModel by viewModels()

    val LocalNavController = staticCompositionLocalOf<NavHostController> {
        error("NavController not provided")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubbrowserTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

                    CompositionLocalProvider(LocalNavController provides navController) {
                        NavHost(
                            navController = navController,
                            startDestination = "page_search" // 起始路由
                        ) {
                            // 声明目的地
                            composable("page_search") {
                                PageSearch(innerPadding)
                            }
                            composable(
                                "page_repository?url={url}", arguments = listOf(
                                    navArgument("url") { defaultValue = "" })
                            ) {
                                it.arguments?.getString("url")?.let { url ->
                                    PageRepository(
                                        innerPadding, url
                                    )
                                }
                            }
                            // 添加更多目的地...
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PageRepository(innerPadding: PaddingValues, url: String) {
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            BaseWebView(url)
        }
    }

    @Composable
    fun PageSearch(innerPadding: PaddingValues) {
        val totalCount by vm.totalCountLiveData.observeAsState()

        var page by remember {
            mutableIntStateOf(1)
        }

        Column(modifier = Modifier.padding(innerPadding)) {
//                        Row{
//                           Button(onClick = { /*TODO*/ }) {
//                               Text(text = "search")
//                           }
//
////                           TextField(value = "", onValueChange = )
//                        }
            var showSearchDialog by remember {
                mutableStateOf(false)
            }

            Button(onClick = {
                showSearchDialog = true
            }) {
                Text(text = "search")
            }

            if (totalCount != null && totalCount != 0) {
                Text(text = "total: $totalCount",
                    modifier = Modifier.padding(5.dp))
            }

            RepositoryFresh(page, onReachedBottom = {
                Log.d(TestActivity.TAG, "RepositoryFresh()")
                vm.searchRepositories(page++)
            })

            SearchDialog(showSearchDialog) { start, key, language ->
                Log.d(TestActivity.TAG, "start = $start, key = $key, language = $language")
                showSearchDialog = false
                if (start) {
                    vm.clearRepositories()
                    vm.searchRepositories(key, language, page++)
                }
            }
        }
    }


    @Composable
    fun RepositoryFresh(page: Int, onReachedBottom: () -> Unit) {
        val repositories by vm.repositoriesLiveData.observeAsState()

        val count = repositories?.size ?: 0

        val listState = rememberLazyListState()

        // 触底判断
        val isAtBottom by remember {
            derivedStateOf {
                val layoutInfo = listState.layoutInfo
                layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
            }
        }

        LaunchedEffect(isAtBottom) {
            Log.d(TAG, "isAtBottom = $isAtBottom")
            if (isAtBottom) onReachedBottom()
        }

        LazyColumn(state = listState) {
            items(repositories ?: emptyList()) {
                val navController = LocalNavController.current
                Column(
                    modifier = Modifier
                        .border(1.5.dp, Color.Gray)
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("page_repository?url=${it.html}")
                        }
                ) {
                    Text(
                        text = it.fullName ?: "",
                        fontSize = 14.sp,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = it.description ?: "",
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                    Row {
                        Text(
                            text = it.language ?: "",
                            fontSize = 10.sp,
                            lineHeight = 14.sp
                        )

                        Icon(
                            imageVector = Icons.Filled.Star, // 或其他变体如 Icons.Outlined.Star
                            modifier = Modifier
                                .padding(start = 8.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                                .size(14.dp),
                            contentDescription = "Star Icon"
                        )

                        Text(
                            text = "${it.stargazers}",
                            fontSize = 10.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }

            if (count != 0) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth() // 占满宽度
                            .padding(16.dp), // 添加内边距
                        contentAlignment = Alignment.Center // 内容居中
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        GithubbrowserTheme {
            Greeting("Android")
        }
    }
}

