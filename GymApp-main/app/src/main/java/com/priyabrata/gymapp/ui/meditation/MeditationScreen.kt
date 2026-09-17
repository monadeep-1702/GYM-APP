package com.priyabrata.gymapp.ui.meditation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.ViewGroup
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.priyabrata.gymapp.service.MeditationAudioService
import com.priyabrata.gymapp.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ─── Data Models ─────────────────────────────────────────────────────────────

enum class TrackType { MP3, YOUTUBE }

data class MeditationTrack(
    val title: String,
    val subtitle: String,
    val audioUrl: String,
    val duration: String,
    val category: String,
    val type: TrackType = TrackType.MP3
)

// ─── Track Data ──────────────────────────────────────────────────────────────

fun getMeditationTracks(): List<MeditationTrack> = listOf(
    MeditationTrack(
        title = "A New Day",
        subtitle = "Start your day with peace",
        audioUrl = "https://github.com/user-attachments/files/30907934/A.new.day.mp3",
        duration = "10:21",
        category = "Morning",
        type = TrackType.MP3
    ),
    MeditationTrack(
        title = "Acceptance",
        subtitle = "Accept with ease",
        audioUrl = "https://github.com/user-attachments/files/30907940/Accaptance.mp3",
        duration = "13:41",
        category = "Morning",
        type = TrackType.MP3
    ),
    MeditationTrack(
        title = "Body Scan",
        subtitle = "Scan your body release tension",
        audioUrl = "https://github.com/user-attachments/files/30907936/Body.scan.mp3",
        duration = "10:12",
        category = "Sitting",
        type = TrackType.MP3
    ),
    MeditationTrack(
        title = "JPMR",
        subtitle = "Muscle relaxation",
        audioUrl = "https://github.com/user-attachments/files/30907943/Jpmr.mp3",
        duration = "14:56",
        category = "Focus",
        type = TrackType.MP3
    ),
    MeditationTrack(
        title = "Breathing Technique",
        subtitle = "4-7-8 Calm breathing",
        audioUrl = "https://github.com/user-attachments/files/30907947/4-7-8.Breathing.mp3",
        duration = "10:33",
        category = "Focus",
        type = TrackType.MP3
    ),
    MeditationTrack(
        title = "I Am Affirmation",
        subtitle = "Affirmation boosts confidence",
        audioUrl = "https://github.com/user-attachments/files/30907938/I.am.mp3",
        duration = "11:00",
        category = "Sleep",
        type = TrackType.MP3
    ),
    MeditationTrack(
        title = "Guided Meditation",
        subtitle = "Deep relaxation",
        audioUrl = "https://youtu.be/W-WQZvkwGhM",
        duration = "10:00",
        category = "Sleep",
        type = TrackType.YOUTUBE
    )
)

// ─── Main Screen ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val tracks = remember { getMeditationTracks() }
    val categories = remember { listOf("All", "Morning", "Sitting", "Focus", "Sleep") }

    var selectedCategory by remember { mutableStateOf("All") }
    var currentPlayingTrack by remember { mutableStateOf<MeditationTrack?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var totalDuration by remember { mutableLongStateOf(0L) }
    var isSeeking by remember { mutableStateOf(false) }
    var seekPosition by remember { mutableLongStateOf(0L) }
    var youTubeReady by remember { mutableStateOf(false) }

    val filteredTracks = remember(selectedCategory) {
        if (selectedCategory == "All") tracks
        else tracks.filter { it.category == selectedCategory }
    }

    // Delay YouTube player render until ExoPlayer fully releases
    LaunchedEffect(currentPlayingTrack) {
        youTubeReady = false
        if (currentPlayingTrack?.type == TrackType.YOUTUBE) {
            delay(800L)
            youTubeReady = true
        }
    }

    // Progress polling for MP3
    LaunchedEffect(currentPlayingTrack, isPlaying) {
        if (currentPlayingTrack?.type == TrackType.MP3 && isPlaying) {
            while (true) {
                val player = MeditationAudioService.getPlayer()
                if (player != null && !isSeeking) {
                    currentPosition = player.currentPosition
                    totalDuration = player.duration.coerceAtLeast(0L)
                }
                delay(500L)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(DarkBg1, DarkBg2))
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ─── Top Bar (no arrow, transparent, dark) ───────────────────────
            TopAppBar(
                title = {
                    Text(
                        "Meditation",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            // ─── YouTube Player (if active) ──────────────────────────────────
            if (currentPlayingTrack?.type == TrackType.YOUTUBE && youTubeReady) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🎬 ${currentPlayingTrack?.title ?: ""}",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "YouTube • Screen stays on",
                                    color = AccentRed,
                                    fontSize = 11.sp
                                )
                            }
                            IconButton(
                                onClick = {
                                    youTubeReady = false
                                    currentPlayingTrack = null
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        YouTubeVideoPlayer(
                            videoUrl = currentPlayingTrack!!.audioUrl,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }

            // ─── Loading indicator while YouTube prepares ────────────────────
            if (currentPlayingTrack?.type == TrackType.YOUTUBE && !youTubeReady) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = AccentCyan,
                                modifier = Modifier.size(32.dp),
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Loading video...",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // ─── MP3 Now Playing Card ────────────────────────────────────────
            if (currentPlayingTrack?.type == TrackType.MP3 && (isPlaying || currentPosition > 0)) {
                NowPlayingCard(
                    track = currentPlayingTrack!!,
                    isPlaying = isPlaying,
                    isBuffering = isBuffering,
                    currentPosition = if (isSeeking) seekPosition else currentPosition,
                    totalDuration = totalDuration,
                    onPlayPause = {
                        val player = MeditationAudioService.getPlayer()
                        if (player != null) {
                            if (player.isPlaying) {
                                val intent = Intent(context, MeditationAudioService::class.java).apply {
                                    action = MeditationAudioService.ACTION_PAUSE
                                }
                                context.startService(intent)
                                isPlaying = false
                            } else {
                                val intent = Intent(context, MeditationAudioService::class.java).apply {
                                    action = MeditationAudioService.ACTION_PLAY
                                }
                                startServiceCompat(context, intent)
                                isPlaying = true
                            }
                        }
                    },
                    onStop = {
                        val intent = Intent(context, MeditationAudioService::class.java).apply {
                            action = MeditationAudioService.ACTION_STOP
                        }
                        context.startService(intent)
                        isPlaying = false
                        currentPlayingTrack = null
                        currentPosition = 0L
                        totalDuration = 0L
                    },
                    onSeekStart = { position ->
                        isSeeking = true
                        seekPosition = position
                    },
                    onSeekChange = { position ->
                        seekPosition = position
                    },
                    onSeekEnd = { position ->
                        MeditationAudioService.getPlayer()?.seekTo(position)
                        currentPosition = position
                        isSeeking = false
                    }
                )
            }

            // ─── Category Chips ──────────────────────────────────────────────
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = category,
                                fontSize = 13.sp,
                                fontWeight = if (selectedCategory == category)
                                    FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ChipSelected.copy(alpha = 0.2f),
                            selectedLabelColor = ChipSelected,
                            containerColor = ChipUnselected,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedCategory == category,
                            selectedBorderColor = ChipSelected.copy(alpha = 0.5f),
                            borderColor = Color.Transparent
                        )
                    )
                }
            }

            // ─── Track List ──────────────────────────────────────────────────
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredTracks) { track ->
                    TrackItem(
                        track = track,
                        isCurrentTrack = currentPlayingTrack == track,
                        isPlaying = isPlaying && currentPlayingTrack == track,
                        onClick = {
                            if (track.type == TrackType.YOUTUBE) {
                                if (currentPlayingTrack?.type == TrackType.MP3) {
                                    val stopIntent = Intent(context, MeditationAudioService::class.java).apply {
                                        action = MeditationAudioService.ACTION_STOP
                                    }
                                    context.startService(stopIntent)
                                }
                                isPlaying = false
                                currentPosition = 0L
                                totalDuration = 0L
                                youTubeReady = false
                                currentPlayingTrack = track
                            } else {
                                youTubeReady = false

                                if (currentPlayingTrack == track && isPlaying) {
                                    val intent = Intent(context, MeditationAudioService::class.java).apply {
                                        action = MeditationAudioService.ACTION_PAUSE
                                    }
                                    context.startService(intent)
                                    isPlaying = false
                                } else if (currentPlayingTrack == track && !isPlaying) {
                                    val intent = Intent(context, MeditationAudioService::class.java).apply {
                                        action = MeditationAudioService.ACTION_PLAY
                                    }
                                    startServiceCompat(context, intent)
                                    isPlaying = true
                                } else {
                                    isBuffering = true
                                    currentPlayingTrack = track
                                    currentPosition = 0L
                                    totalDuration = 0L

                                    val intent = Intent(context, MeditationAudioService::class.java).apply {
                                        action = MeditationAudioService.ACTION_PLAY
                                        putExtra(MeditationAudioService.EXTRA_AUDIO_URL, track.audioUrl)
                                        putExtra(MeditationAudioService.EXTRA_TITLE, track.title)
                                    }
                                    startServiceCompat(context, intent)
                                    isPlaying = true

                                    scope.launch {
                                        delay(3000L)
                                        isBuffering = false
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

// ─── Now Playing Card ────────────────────────────────────────────────────────

@Composable
private fun NowPlayingCard(
    track: MeditationTrack,
    isPlaying: Boolean,
    isBuffering: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    onPlayPause: () -> Unit,
    onStop: () -> Unit,
    onSeekStart: (Long) -> Unit,
    onSeekChange: (Long) -> Unit,
    onSeekEnd: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🎵 ${track.title}",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(
                text = track.subtitle,
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (totalDuration > 0) {
                val progress = currentPosition.toFloat() / totalDuration.toFloat()

                Slider(
                    value = progress.coerceIn(0f, 1f),
                    onValueChange = { value ->
                        val newPosition = (value * totalDuration).toLong()
                        onSeekStart(newPosition)
                        onSeekChange(newPosition)
                    },
                    onValueChangeFinished = {
                        onSeekEnd(currentPosition)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = AccentCyan,
                        activeTrackColor = AccentCyan,
                        inactiveTrackColor = TextSecondary.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(formatTime(currentPosition), color = TextSecondary, fontSize = 11.sp)
                    Text(formatTime(totalDuration), color = TextSecondary, fontSize = 11.sp)
                }
            } else if (isBuffering) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = AccentCyan,
                    trackColor = TextSecondary.copy(alpha = 0.2f)
                )
                Text("Buffering...", color = TextSecondary, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onStop,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Stop,
                        contentDescription = "Stop",
                        tint = AccentCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(
                    onClick = onPlayPause,
                    modifier = Modifier
                        .size(56.dp)
                        .background(AccentCyan.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = AccentCyan,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

// ─── Track List Item ─────────────────────────────────────────────────────────

@Composable
private fun TrackItem(
    track: MeditationTrack,
    isCurrentTrack: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    val bgAlpha by animateFloatAsState(
        targetValue = if (isCurrentTrack) 0.15f else 0.05f,
        label = "trackBgAlpha"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isCurrentTrack) AccentCyan.copy(alpha = 0.5f) else Color.Transparent,
        label = "trackBorder"
    )
    val typeColor = if (track.type == TrackType.YOUTUBE) AccentRed else AccentCyan
    val typeIcon = if (track.type == TrackType.YOUTUBE) "🎬" else "🎵"
    val typeLabel = if (track.type == TrackType.YOUTUBE) "YT" else "MP3"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBg.copy(alpha = if (isCurrentTrack) 1f else 0.7f)
        ),
        border = if (isCurrentTrack) androidx.compose.foundation.BorderStroke(
            1.dp, borderColor
        ) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = typeIcon, fontSize = 24.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    color = if (isCurrentTrack) AccentCyan else TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (track.subtitle.isNotEmpty()) {
                    Text(
                        text = track.subtitle,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Text(text = track.duration, color = TextSecondary, fontSize = 12.sp)

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .background(typeColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = typeLabel, color = typeColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            if (isCurrentTrack && isPlaying) {
                Spacer(modifier = Modifier.width(6.dp))
                Text("♪", color = AccentCyan, fontSize = 16.sp)
            }
        }
    }
}

// ─── YouTube Video Player (WebView) ─────────────────────────────────────────

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun YouTubeVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val videoId = remember(videoUrl) {
        val patterns = listOf(
            Regex("youtu\\.be/([a-zA-Z0-9_-]{11})"),
            Regex("[?&]v=([a-zA-Z0-9_-]{11})"),
            Regex("embed/([a-zA-Z0-9_-]{11})")
        )
        patterns.firstNotNullOfOrNull { it.find(videoUrl)?.groupValues?.get(1) } ?: ""
    }

    if (videoId.isEmpty()) return

    val html = remember(videoId) {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">
            <style>
                * { margin:0; padding:0; overflow:hidden; }
                body { background:#000; }
                .container { position:relative; width:100%; padding-bottom:56.25%; height:0; }
                iframe { position:absolute; top:0; left:0; width:100%; height:100%; border:0; }
            </style>
        </head>
        <body>
            <div class="container">
                <iframe 
                    src="https://www.youtube-nocookie.com/embed/$videoId?autoplay=1&playsinline=1&rel=0&modestbranding=1&iv_load_policy=3"
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                    allowfullscreen>
                </iframe>
            </div>
        </body>
        </html>
        """.trimIndent()
    }

    var webView by remember { mutableStateOf<android.webkit.WebView?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            webView?.stopLoading()
            webView?.loadUrl("about:blank")
            webView?.destroy()
            webView = null
        }
    }

    AndroidView(
        factory = { context ->
            android.webkit.WebView(context).apply {
                webView = this
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(android.graphics.Color.BLACK)
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT

                webViewClient = android.webkit.WebViewClient()
                webChromeClient = android.webkit.WebChromeClient()

                loadDataWithBaseURL(
                    "https://www.youtube-nocookie.com",
                    html,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        modifier = modifier
    )
}

// ─── Helper Functions ────────────────────────────────────────────────────────

private fun formatTime(ms: Long): String {
    if (ms <= 0) return "0:00"
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

private fun startServiceCompat(context: Context, intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}