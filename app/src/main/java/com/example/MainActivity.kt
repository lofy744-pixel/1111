package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.navigation.NavGraph
import com.example.ui.theme.NeovaTheme
import com.example.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

  private val mainViewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val isDarkMode by mainViewModel.isDarkMode.collectAsState()

      NeovaTheme(darkTheme = isDarkMode) {
        Surface(
          modifier = Modifier.fillMaxSize()
        ) {
          NavGraph(viewModel = mainViewModel)
        }
      }
    }
  }
}

