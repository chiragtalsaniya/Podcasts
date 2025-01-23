package com.audiobooks.podcasts
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.audiobooks.podcasts.ui.PodcastListScreen
import com.audiobooks.podcasts.ui.theme.PodcastsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PodcastsTheme {
                val navController = rememberNavController()
                PodcastListScreen(
                    onPodcastClick = { podcast ->
                        // Handle navigation or podcast click
                        // For now, just log or handle this action
                    }
                )
            }
        }
    }
}