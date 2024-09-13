package com.iucoding.chatgptclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.iucoding.chatgptclient.composable.MainScreen
import com.iucoding.chatgptclient.repository.ChatGptFacade
import com.iucoding.chatgptclient.ui.theme.ChatGPTClientTheme
import com.iucoding.chatgptclient.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by lazy {
        MainViewModel(
            ChatGptFacade.getInstance(ChatGptFacade.Engine.GENERATIVE_AI)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatGPTClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
