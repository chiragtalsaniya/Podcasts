package com.audiobooks.podcasts.ui

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.audiobooks.podcasts.R
import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.ui.theme.PodcastsTheme
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

@Composable
fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
