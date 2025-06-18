package com.hef.githubbrowser.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hef.githubbrowser.TestActivity
import com.hef.githubbrowser.ui.page.BaseWebView
import com.hef.githubbrowser.ui.page.MinePrimaryPage
import com.hef.githubbrowser.ui.page.SearchDialog
import com.hef.githubbrowser.ui.theme.GithubbrowserTheme
import kotlinx.coroutines.launch

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/17
 */
class MainActivity : ComponentActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private val vm: MainViewModel by viewModels()

    private val LocalNavController = staticCompositionLocalOf<NavHostController> {
        error("NavController not provided")
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubbrowserTheme {
                val pages = listOf("Search", "Me")
                val pagerState = rememberPagerState(pageCount = { pages.size })
                val coroutineScope = rememberCoroutineScope()

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            pages.forEachIndexed { index, title ->
                                NavigationBarItem(
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(
                                                index
                                            )
                                        }
                                    },
                                    icon = {
                                        when (index) {
                                            0 -> Icon(
                                                imageVector = Icons.Filled.Search, // 或其他变体如 Icons.Outlined.Star
                                                modifier = Modifier
                                                    .padding(
                                                        start = 0.dp,
                                                        top = 0.dp,
                                                        end = 0.dp,
                                                        bottom = 0.dp
                                                    )
                                                    .size(18.dp),
                                                contentDescription = "Search Icon"
                                            )

                                            1 -> Icon(
                                                imageVector = Icons.Filled.Face, // 或其他变体如 Icons.Outlined.Star
                                                modifier = Modifier
                                                    .padding(
                                                        start = 0.dp,
                                                        top = 0.dp,
                                                        end = 0.dp,
                                                        bottom = 0.dp
                                                    )
                                                    .size(18.dp),
                                                contentDescription = "Mine Icon"
                                            )
                                        }

                                    },

                                    label = { Text(title, fontSize = 18.sp) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    HorizontalPager(
                        state = pagerState,
                        beyondBoundsPageCount = 1,
                        userScrollEnabled = false,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) { pageIndex ->
                        when (pageIndex) {
                            0 -> SearchPrimaryPage()
                            1 -> MinePrimaryPage()
                            // ...
                        }
                    }
                }
            }
        }
    }

//    @Composable
//    fun HomeScreen() {
//        Text(text = "home")
//    }

    @Composable
    fun SearchPrimaryPage(){
        val navController = rememberNavController()

        CompositionLocalProvider(LocalNavController provides navController) {
            NavHost(
                navController = navController,
                startDestination = "page_search" // 起始路由
            ) {
                // 声明目的地
                composable("page_search") {
                    PageSearch()
                }
                composable(
                    "page_repository?url={url}", arguments = listOf(
                        navArgument("url") { defaultValue = "" })
                ) {
                    it.arguments?.getString("url")?.let { url ->
                        BaseWebView(url = url)
                    }
                }
                // 添加更多目的地...
            }
        }
    }

    @Composable
    fun PageSearch() {
        val totalCount by vm.totalCountLiveData.observeAsState()

        var page by remember {
            mutableIntStateOf(1)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            var showSearchDialog by remember {
                mutableStateOf(false)
            }
            
            Row {
                Icon(
                    imageVector = Icons.Filled.Search, // 或其他变体如 Icons.Outlined.Star
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            top = 6.dp,
                            end = 5.dp,
                            bottom = 0.dp
                        )
                        .size(26.dp)
                        .clickable {
                            showSearchDialog = true
                        },
                    contentDescription = "Mine Icon"
                )
                
                Text(text = "Click here to set search keywords.", fontSize = 18.sp, modifier = Modifier.padding(start = 0.dp, top = 6.dp, end = 0.dp, bottom = 0.dp))
            }

            if (totalCount != null && totalCount != 0) {
                Text(text = "total: $totalCount",
                    modifier = Modifier.padding(5.dp))
            }

            RefreshRepositories(onReachedBottom = {
                Log.d(TestActivity.TAG, "RepositoryFresh()")
                if(!vm.isAllLoaded()) {
                    vm.searchRepositories(page++)
                }
            })

            SearchDialog(showSearchDialog) { start, key, language ->
                Log.d(TestActivity.TAG, "start = $start, key = $key, language = $language")
                showSearchDialog = false
                if (start) {
                    vm.clearRepositories()
                    page = 1
                    vm.searchRepositories(key, language, page++)
                }
            }
        }
    }

    @Composable
    fun RefreshRepositories(onReachedBottom: () -> Unit) {
        val repositories by vm.repositoriesLiveData.observeAsState()

        val isloading by vm.loadingLiveData.observeAsState()

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
            Log.d(TestActivity.TAG, "isAtBottom = $isAtBottom")
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

            if (count != 0 && isloading == true) {
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
}


