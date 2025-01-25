package com.audiobooks.podcasts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.audiobooks.podcasts.ui.podcast.detail.PodcastDetailScreen
import com.audiobooks.podcasts.ui.podcast.detail.SharedPodcastViewModel
import com.audiobooks.podcasts.ui.podcast.list.PodcastListScreen
import com.audiobooks.podcasts.ui.theme.PodcastsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PodcastsTheme {
                val navController = rememberNavController()
                val sharedPodcastViewModel: SharedPodcastViewModel = hiltViewModel()

                NavHost(
                    navController = navController, startDestination = "podcast_list"
                ) {
                    composable("podcast_list") {
                        PodcastListScreen(onPodcastClick = { podcast ->
                            sharedPodcastViewModel.selectPodcast(podcast)
                            navController.navigate("podcast_detail")
                        })
                    }
                    composable("podcast_detail") {
                        PodcastDetailScreen(
                            viewModel = sharedPodcastViewModel, onBack = {
                                navController.popBackStack()
                            })

                    }
                }
            }
        }
    }
}
