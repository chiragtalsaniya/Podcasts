package com.audiobooks.podcasts.ui.podcast.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.audiobooks.podcasts.R
import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.utils.ResultResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastListScreen(
    onPodcastClick: (Podcast) -> Unit,
    viewModel: PodcastListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.app_name)) },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (uiState) {
                    is ResultResponse.Loading -> LoadingIndicator()
                    is ResultResponse.Success -> PodcastList(
                        podcasts = (uiState as ResultResponse.Success).data,
                        onPodcastClick = onPodcastClick,
                        loadMorePodcasts = { viewModel.loadPodcasts() },
                        isLoading = isLoading
                    )
                    is ResultResponse.Error -> ErrorMessage(
                        message = (uiState as ResultResponse.Error).exception
                    )
                }
            }
        }

}

@Preview(showBackground = true)
@Composable
fun LoadingIndicator(message: String = stringResource(R.string.loading_podcasts)) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


@Composable
fun ErrorMessage(message: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun PodcastList(
    podcasts: List<Podcast>,
    onPodcastClick: (Podcast) -> Unit,
    loadMorePodcasts: () -> Unit, // Trigger to load more podcasts
    isLoading: Boolean // Show progress indicator only when loading
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(podcasts) { podcast ->
            PodcastItem(
                podcast = podcast,
                onClick = { onPodcastClick(podcast) }
            )
        }

        // Show the progress bar only when loading
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        // Detect end of list and trigger pagination
        item {
            LaunchedEffect(podcasts.size) {
                if (!isLoading) {
                    loadMorePodcasts()
                }
            }
        }
    }
}


@Composable
fun PodcastItem(
    podcast: Podcast,
    onClick: () -> Unit
) {
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(podcast.thumbnail),
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp) // Increased size for larger image
                        .aspectRatio(1f) // Keep the image square
                        .clip(MaterialTheme.shapes.medium) // Apply rounded corners
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = podcast.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = podcast.publisher,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (podcast.isFavourite) {
                        Text(
                            text = stringResource(R.string.favourited),
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorMessagePreview() {
    ErrorMessage(message = "Failed to load podcasts. Please try again.")
}
@Preview(showBackground = true)
@Composable
fun PodcastListPreview() {
    PodcastList(
        podcasts = listOf(
            Podcast(
                id = "1",
                title = "Masters of Scale",
                description = "Award-winning business advice from Silicon Valley and beyond.",
                publisher = "WaitWhat",
                thumbnail = "https://cdn-images-3.listennotes.com/podcasts/masters-of-scale-waitwhat-pC8IU6xO9LK-mYoV0CUyxTD.300x300.jpg",
                totalEpisodes = 456,
                image = "https://cdn-images-3.listennotes.com/podcasts/masters-of-scale-waitwhat-w5Tb9hPCs-8-mYoV0CUyxTD.1400x1400.jpg",
                isFavourite = true,
                rss = null,
                website = "http://www.mastersofscale.com",
                genreIds = emptyList(),
                language = "English",
                country = "United States",
                explicitContent = false,
                listennotesUrl = "https://www.listennotes.com/c/d863da7f921e435fb35f512b54e774d6/",
                audioLengthSec = 2018,
                latestPubDateMs = 1713430800000,
                earliestPubDateMs = 1492543297432,
                hasSponsors = false,
                email = "hello@mastersofscale.com",
                latestEpisodeId = "a8841be2af14462aa6054ec020faed01",
                isClaimed = false,
                listenScore = 71,
                listenScoreGlobalRank = "0.05%",
                hasGuestInterviews = true,
                lookingFor = null,
                socialLinks = null
            ),
            Podcast(
                id = "2",
                title = "The Smart Passive Income Online Business and Blogging Podcast",
                description = "Pat Flynn reveals all of his online business strategies.",
                publisher = "Pat Flynn",
                thumbnail = "https://cdn-images-3.listennotes.com/podcasts/the-smart-passive-income-online-business-sF24owQHYWy-NDa6-ySp9kw.300x300.jpg",
                totalEpisodes = 790,
                image = "https://cdn-images-3.listennotes.com/podcasts/the-smart-passive-income-online-business-jN-aR6qdYuo-NDa6-ySp9kw.1400x1400.jpg",
                isFavourite = false,
                rss = null,
                website = "https://art19.com/shows/smart-passive-income-podcast",
                genreIds = emptyList(),
                language = "English",
                country = "United States",
                explicitContent = false,
                listennotesUrl = "https://www.listennotes.com/c/499661f3589f42aaa1532673e0e0aedf/",
                audioLengthSec = 2424,
                latestPubDateMs = 1713337200000,
                earliestPubDateMs = 1279551600767,
                hasSponsors = true,
                email = "podcasts@teamspi.com",
                latestEpisodeId = "c8a8e517dba345cab1c80e36d7e4e18c",
                isClaimed = false,
                listenScore = 70,
                listenScoreGlobalRank = "0.05%",
                hasGuestInterviews = true,
                lookingFor = null,
                socialLinks = null
            )
        ),
        onPodcastClick = { /* Handle click */ },
        loadMorePodcasts = { /* Trigger loading more podcasts */ },
        isLoading = false // Simulate non-loading state
    )
}
