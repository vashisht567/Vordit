package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entities.SellerProfileEntity
import com.example.ui.theme.OasisGreen
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron
import com.example.ui.theme.StarGold
import kotlinx.coroutines.delay

@Composable
fun MeetTheMakerSection(
    seller: SellerProfileEntity?,
    fallbackMakerName: String = "",
    fallbackMakerStory: String = "",
    fallbackDistrict: String = "Rajasthan",
    currentLanguage: String = "HI",
    onEditClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isVideoPlaying by remember { mutableStateOf(false) }
    var isFullscreenVideoOpen by remember { mutableStateOf(false) }
    var videoProgress by remember { mutableStateOf(0f) }
    var isMuted by remember { mutableStateOf(false) }
    var showAppreciationSent by remember { mutableStateOf(false) }

    val artisanName = seller?.artisanName?.ifBlank { null } ?: fallbackMakerName.ifBlank { "Master Artisan" }
    val businessName = seller?.businessName?.ifBlank { null } ?: "Rajasthani Artisan Cooperative"
    val district = seller?.district?.ifBlank { null } ?: fallbackDistrict
    val villageOrCity = seller?.villageOrCity ?: ""
    val locationText = if (villageOrCity.isNotBlank()) "$villageOrCity, $district, Rajasthan" else "$district, Rajasthan"
    
    val craftOrigin = seller?.craftOrigin?.ifBlank { null }
        ?: "Ancestral Rajasthani handmade craft technique handed down across generations using local natural desert materials."
    
    val artisanStory = seller?.artisanStory?.ifBlank { null }
        ?: fallbackMakerStory.ifBlank { "Dedicated artisan preserving ancestral heritage through authentic handmade craftsmanship." }

    val hasVideo = !seller?.videoUrl.isNullOrBlank() || !seller?.videoTitle.isNullOrBlank()
    val videoTitle = seller?.videoTitle?.ifBlank { null }
        ?: if (currentLanguage == "HI") "कार्यशाला वीडियो: शिल्प निर्माण प्रक्रिया" else "Artisan at Work: Traditional Crafting Process"
    val totalSeconds = if ((seller?.videoDurationSeconds ?: 0) > 0) seller!!.videoDurationSeconds else 48

    // Simulate video playback progress
    LaunchedEffect(isVideoPlaying) {
        if (isVideoPlaying) {
            while (isVideoPlaying) {
                delay(200)
                videoProgress += 0.2f / totalSeconds
                if (videoProgress >= 1f) {
                    videoProgress = 0f
                    isVideoPlaying = false
                }
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("meet_the_maker_section"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Section Header with Royal Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(RoyalSaffron.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = RoyalSaffron,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = if (currentLanguage == "HI") "कारीगर से मिलें" else "Meet the Maker",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (currentLanguage == "HI") "सीधे गाँव के हुनरमंद हाथों से" else "Direct from Rural Artisan Hands",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (onEditClick != null) {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.testTag("edit_maker_profile_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = RoyalIndigo
                        )
                    }
                } else {
                    // Verified Heritage Badge
                    Surface(
                        color = OasisGreen.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = OasisGreen, modifier = Modifier.size(14.dp))
                            Text(
                                text = if (currentLanguage == "HI") "प्रमाणित कारीगर" else "Verified Maker",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OasisGreen
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Maker Info Row: Avatar + Name + Business + Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Artisan Royal Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(RoyalSaffron, RoyalIndigo)
                            )
                        )
                        .border(2.dp, StarGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = artisanName.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                        color = Color.White
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = artisanName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = businessName,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = RoyalSaffron
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = StarGold, modifier = Modifier.size(14.dp))
                        Text(
                            text = "${seller?.rating ?: 4.9} ★ (${seller?.reviewCount ?: 28} orders)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Location Badge & Craft Origin Pill
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = RoyalIndigo, modifier = Modifier.size(16.dp))
                        Text(
                            text = locationText,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.HistoryEdu, contentDescription = null, tint = RoyalSaffron, modifier = Modifier.size(16.dp))
                        Column {
                            Text(
                                text = if (currentLanguage == "HI") "शिल्प उद्भव व विरासत:" else "Craft Origin & Lineage:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = RoyalSaffron
                            )
                            Text(
                                text = craftOrigin,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Detailed Artisan Story
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = if (currentLanguage == "HI") "कारीगर की कहानी (The Maker's Story)" else "The Maker's Journey",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = artisanStory,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    lineHeight = 20.sp
                )
            }

            // Short Video Reel Player (if available)
            if (hasVideo) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Videocam, contentDescription = null, tint = RoyalSaffron, modifier = Modifier.size(18.dp))
                            Text(
                                text = if (currentLanguage == "HI") "कारीगर कार्यशाला वीडियो" else "Workshop Reel & Making Video",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        TextButton(
                            onClick = { isFullscreenVideoOpen = true },
                            modifier = Modifier.testTag("expand_maker_video_btn")
                        ) {
                            Icon(Icons.Default.Fullscreen, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (currentLanguage == "HI") "बड़ा करें" else "Full Video", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    // Video Player Surface
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF1A1A24), Color(0xFF2C1810))
                                )
                            )
                            .border(1.dp, RoyalSaffron.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .clickable { isVideoPlaying = !isVideoPlaying }
                            .testTag("maker_video_player_box"),
                        contentAlignment = Alignment.Center
                    ) {
                        // Background Workshop Ambience Graphics
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = if (isVideoPlaying) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled,
                                contentDescription = if (isVideoPlaying) "Pause" else "Play",
                                tint = if (isVideoPlaying) StarGold else Color.White,
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = videoTitle,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color.White.copy(alpha = 0.9f),
                                maxLines = 1
                            )
                            Text(
                                text = if (isVideoPlaying) {
                                    val currentSec = (videoProgress * totalSeconds).toInt()
                                    "Playing: 0:${currentSec.toString().padStart(2, '0')} / 0:${totalSeconds}"
                                } else {
                                    if (currentLanguage == "HI") "वीडियो देखने के लिए टैप करें (${totalSeconds}s)" else "Tap to watch making reel (${totalSeconds}s)"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = StarGold
                            )
                        }

                        // Top Badges Overlay
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color.Black.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(if (isVideoPlaying) Color.Red else OasisGreen)
                                    )
                                    Text(
                                        text = if (isVideoPlaying) "REEL PLAYING" else "ORIGINAL FOOTAGE",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            }

                            IconButton(
                                onClick = { isMuted = !isMuted },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                    contentDescription = "Audio toggle",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Bottom Scrub Bar
                        LinearProgressIndicator(
                            progress = { videoProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .height(4.dp),
                            color = RoyalSaffron,
                            trackColor = Color.White.copy(alpha = 0.2f),
                        )
                    }
                }
            }

            // Authentic Craft Guarantee & Fair Trade Direct Impact
            Surface(
                color = RoyalSaffron.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Handshake,
                        contentDescription = null,
                        tint = RoyalSaffron,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (currentLanguage == "HI") "100% प्रत्यक्ष कारीगर लाभ" else "100% Direct Artisan Fair Trade",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (currentLanguage == "HI") "कोई बिचौलिया नहीं। आपकी खरीद सीधे ${artisanName} के परिवार व गाँव का सशक्तिकरण करती है।" 
                            else "Zero middleman markups. Your purchase directly supports $artisanName and rural artisan livelihood.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Interactive Namaste / Appreciation Button
            OutlinedButton(
                onClick = { showAppreciationSent = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("send_namaste_artisan_btn"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (showAppreciationSent) {
                        if (currentLanguage == "HI") "कारीगर को सम्मान भेजा गया! 🙏" else "Namaste & Appreciation Sent! 🙏"
                    } else {
                        if (currentLanguage == "HI") "कारीगर को नमस्ते व शुभकामनाएं भेजें 🙏" else "Send Namaste & Artisan Appreciation 🙏"
                    },
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }

    // Fullscreen Video Modal Dialog
    if (isFullscreenVideoOpen) {
        Dialog(
            onDismissRequest = { isFullscreenVideoOpen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                color = Color.Black
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = videoTitle,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = "$artisanName • $locationText",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = StarGold
                                )
                            }

                            IconButton(
                                onClick = { isFullscreenVideoOpen = false },
                                modifier = Modifier.testTag("close_fullscreen_video_btn")
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                            }
                        }

                        // Center Simulated Video Frame
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(Color(0xFF3E2723), Color(0xFF0D0D12))
                                    )
                                )
                                .clickable { isVideoPlaying = !isVideoPlaying },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (isVideoPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                    contentDescription = null,
                                    tint = StarGold,
                                    modifier = Modifier.size(72.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = if (isVideoPlaying) "Playing Workshop Reel..." else "Tap to Play Reel",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "\"$craftOrigin\"",
                                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                                    color = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }

                            // Subtitle overlay
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 16.dp)
                                    .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (currentLanguage == "HI") "कारीगर की हस्तकला: पारंपरिक राजस्थानी शिल्प तकनीक" else "Artisan at Work: Preserving Rajasthan's Handmade Heritage",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        // Bottom Controls
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val currentSec = (videoProgress * totalSeconds).toInt()
                                Text(
                                    text = "0:${currentSec.toString().padStart(2, '0')} / 0:${totalSeconds}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    IconButton(onClick = { isMuted = !isMuted }) {
                                        Icon(
                                            imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                            contentDescription = "Audio",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }

                            Slider(
                                value = videoProgress,
                                onValueChange = { videoProgress = it },
                                colors = SliderDefaults.colors(
                                    thumbColor = RoyalSaffron,
                                    activeTrackColor = RoyalSaffron,
                                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                                )
                            )

                            Button(
                                onClick = { isFullscreenVideoOpen = false },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron)
                            ) {
                                Text(if (currentLanguage == "HI") "उत्पाद पर वापस जाएँ" else "Back to Product")
                            }
                        }
                    }
                }
            }
        }
    }
}
