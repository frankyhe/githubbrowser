package com.hef.githubbrowser.ui.page

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hef.githubbrowser.model.GithubLoginChecker

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/17
 */

@Composable
fun MinePrimaryPage() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "page_guide" // 起始路由
    ) {
        // 声明目的地
        composable("page_guide") {
            PageGuide(navController)
        }
        composable("page_logout") {
            PageLogout()
        }
        composable("page_my_profile"){
            PageMyProfile()
        }
    }
}

@Composable
fun PageGuide(navController: NavHostController){
    var login by remember{
        mutableStateOf(GithubLoginChecker.isLogin())
    }

    if(login){
        PageMySettings(navController)
    } else {
        LoginWebView(GithubLoginChecker.LOGIN_URL){
            if(it){
                login = true
            }
        }
    }
}

@Composable
fun PageMySettings(navController: NavHostController){
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "My Profile",
            fontSize = 32.sp,
            modifier = Modifier
                .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 2.dp)
                .fillMaxWidth()
                .border(2.dp, Color.Gray)
                .clickable {
                    navController.navigate("page_my_profile")
                }
        )
        Text(
            text = "Logout",
            fontSize = 32.sp,
            modifier = Modifier
                .padding(start = 10.dp, top = 2.dp, end = 10.dp, bottom = 2.dp)
                .fillMaxWidth()
                .border(2.dp, Color.Gray)
                .clickable {
                    navController.navigate("page_logout")
                }
        )
    }
}

@Composable
fun PageMyProfile(){
    BaseWebView(url = GithubLoginChecker.LOGIN_URL)
}

@Composable
fun PageLogout(){
    BaseWebView(url = GithubLoginChecker.LOGOUT_URL)
}