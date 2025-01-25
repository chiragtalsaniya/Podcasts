package com.audiobooks.podcasts.ui.podcast.detail
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.audiobooks.podcasts.domain.model.Podcast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastDetailScreen(
    viewModel: SharedPodcastViewModel? = null, // Optional ViewModel for production
    podcast: Podcast? = viewModel?.selectedPodcast?.collectAsState()?.value, // Use podcast directly if provided
    onBack: () -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable(onClick = onBack)
                            .padding(horizontal = 8.dp) // Adjust horizontal padding for proper spacing
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, // Use appropriate icon
                            contentDescription = "Back Icon",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
                        Text(
                            text = "Back",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                title = {}, // Leave the title empty if you want no additional text
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            podcast?.let {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        // Title
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 24.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Publisher
                        Text(
                            text = it.publisher,
                            style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Thumbnail with dynamic aspect ratio
                        Image(
                            painter = rememberAsyncImagePainter(it.thumbnail),
                            contentDescription = "Podcast Thumbnail",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(screenWidth * 0.7f) // Use 70% of screen width
                                .aspectRatio(1f) // Maintain a 1:1 aspect ratio
                                .clip(RoundedCornerShape(16.dp))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Favourite Button with Animation and Snackbar
                        val buttonColor by animateColorAsState(
                            targetValue = if (it.isFavourite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )

                        Button(
                            onClick = {
                                viewModel?.toggleFavourite()
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss() // Dismiss the current Snackbar if any
                                    snackbarHostState.showSnackbar(
                                        message = if (it.isFavourite) "Removed from favourites" else "Added to favourites",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = buttonColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                        ) {
                            Text(
                                text = if (it.isFavourite) "Favourited" else "Favourite",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Description (Scrollable if long)
                    item {
                        Text(
                            text = it.description,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PodcastDetailScreenPreview() {
    PodcastDetailScreen(
        podcast = Podcast(
            id = "1",
            title = "Masters of Scale",
            description = "Award-winning business advice from Silicon Valley and beyond. Iconic CEOs share their strategies to scale businesses successfully.",
            publisher = "WaitWhat",
            thumbnail = "https://cdn-images-3.listennotes.com/podcasts/masters-of-scale-waitwhat-pC8IU6xO9LK-mYoV0CUyxTD.300x300.jpg",
            totalEpisodes = 456,
            image = "",
            isFavourite = true,
            rss = null,
            website = null,
            genreIds = emptyList(),
            language = "English",
            country = "United States",
            explicitContent = false,
            listennotesUrl = "",
            audioLengthSec = 0,
            latestPubDateMs = 0L,
            earliestPubDateMs = 0L,
            hasSponsors = false,
            email = null,
            latestEpisodeId = "",
            isClaimed = false,
            listenScore = 0,
            listenScoreGlobalRank = "",
            hasGuestInterviews = false,
            lookingFor = null,
            socialLinks = null
        ),
        onBack = { /* Handle back navigation */ }
    )
}
