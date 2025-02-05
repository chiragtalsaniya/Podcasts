package com.audiobooks.podcasts.ui.podcast.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.audiobooks.podcasts.R
import com.audiobooks.podcasts.domain.model.Podcast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastListScreen(
    onPodcastClick: (Podcast) -> Unit, viewModel: PodcastListViewModel = hiltViewModel()
) {
    val podcasts = viewModel.podcasts.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        coroutineScope.launch {
            podcasts.refresh()
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(R.string.app_name)) },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            actions = {
                IconButton(onClick = {
                    coroutineScope.launch {
                        podcasts.refresh()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh List",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                podcasts.loadState.refresh is LoadState.Loading -> {
                    LoadingIndicator("Loading podcasts...")
                }

                podcasts.itemCount == 0 -> {
                    EmptyState("No podcasts available.")
                }

                else -> {
                    PodcastList(
                        podcasts = podcasts, onPodcastClick = onPodcastClick
                    )
                }
            }
        }
    }
}

@Composable
fun PodcastList(
    podcasts: LazyPagingItems<Podcast>, onPodcastClick: (Podcast) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(podcasts.itemCount) { index ->
            val podcast = podcasts[index]
            podcast?.let {
                PodcastItem(podcast = it, onClick = { onPodcastClick(it) })
            }
        }

        // Handle Pagination Loading every 20 items
        if (podcasts.itemCount % 20 == 0) {
            when (podcasts.loadState.append) {
                is LoadState.Loading -> {
                    item { PaginationLoadingIndicator() }
                }

                is LoadState.Error -> {
                    item { RetryButton { podcasts.retry() } }
                }

                else -> Unit
            }
        }
    }
}

@Composable
fun LoadingIndicator(message: String) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
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
fun EmptyState(message: String) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun PaginationLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun RetryButton(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun PodcastItem(
    podcast: Podcast, onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(podcast.thumbnail),
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = podcast.title, style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface
                    ), maxLines = 2, overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = podcast.publisher, style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.5f
                        )
                    ), maxLines = 1, overflow = TextOverflow.Ellipsis
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
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    }
}
