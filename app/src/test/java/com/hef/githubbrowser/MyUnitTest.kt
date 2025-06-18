package com.hef.githubbrowser

import com.hef.githubbrowser.model.GitHubModel
import com.hef.githubbrowser.model.GitHubUtils
import com.hef.githubbrowser.model.GithubLoginChecker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MyUnitTest {
//    @Test
//    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//    }

    @Test
    fun checkGitHubUtils(){
        assertEquals("koolbelt language:kotlin", GitHubUtils.getQueryKey("koolbelt", "kotlin"))
        assertEquals("koolbelt", GitHubUtils.getQueryKey("koolbelt", null))
        assertEquals("koolbelt", GitHubUtils.getQueryKey("koolbelt", ""))
        assertEquals("language:kotlin", GitHubUtils.getQueryKey(null, "kotlin"))
        assertEquals("language:kotlin", GitHubUtils.getQueryKey("", "kotlin"))
        assertEquals("language:kotlin", GitHubUtils.getQueryKey(null, null))
        assertEquals("language:kotlin", GitHubUtils.getQueryKey("", ""))
    }

    @Test
    fun checkGitHubModel(){
        GlobalScope.launch {
            var response = GitHubModel.api.searchRepositories(query = "language:kotlin", page = 20)
            assertNotNull(response)
        }
    }

//    @Test
//    fun checkGithubLoginChecker(){
//        assertTrue(GithubLoginChecker.isLogin("dafkadfladflakdfdakfkdkflogged_in=yesikdadfadfdafa"))
//        assertFalse(GithubLoginChecker.isLogin("dafkadfladflakdfdakfkdkflog=yesikdadfadfdafa"))
//    }
}