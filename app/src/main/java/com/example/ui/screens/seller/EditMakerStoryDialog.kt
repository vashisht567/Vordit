package com.example.ui.screens.seller

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entities.SellerProfileEntity
import com.example.ui.components.MeetTheMakerSection
import com.example.ui.theme.OasisGreen
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron
import com.example.ui.theme.StarGold

data class VideoPreset(
    val title: String,
    val hindiTitle: String,
    val durationSeconds: Int,
    val url: String,
    val craftOriginSuggestion: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMakerStoryDialog(
    seller: SellerProfileEntity,
    currentLanguage: String = "HI",
    onDismiss: () -> Unit,
    onSave: (
        artisanName: String,
        businessName: String,
        district: String,
        villageOrCity: String,
        fullAddress: String,
        craftOrigin: String,
        artisanStory: String,
        videoUrl: String,
        videoTitle: String,
        videoDurationSeconds: Int
    ) -> Unit
) {
    var artisanName by remember { mutableStateOf(seller.artisanName) }
    var businessName by remember { mutableStateOf(seller.businessName) }
    var district by remember { mutableStateOf(seller.district) }
    var villageOrCity by remember { mutableStateOf(seller.villageOrCity) }
    var fullAddress by remember { mutableStateOf(seller.fullAddress) }
    var craftOrigin by remember { mutableStateOf(seller.craftOrigin.ifBlank { "Ancestral handmade craft technique handed down across generations." }) }
    var artisanStory by remember { mutableStateOf(seller.artisanStory) }
    var videoUrl by remember { mutableStateOf(seller.videoUrl) }
    var videoTitle by remember { mutableStateOf(seller.videoTitle.ifBlank { "Traditional Crafting Process in Workshop" }) }
    var videoDurationSeconds by remember { mutableStateOf(if (seller.videoDurationSeconds > 0) seller.videoDurationSeconds else 45) }

    var isDistrictDropdownOpen by remember { mutableStateOf(false) }
    var showPreview by remember { mutableStateOf(false) }

    val districts = listOf(
        "Banswara", "Jaipur", "Barmer", "Jodhpur", "Jaisalmer", "Udaipur", "Kota", "Dungarpur", "Bikaner", "Chittorgarh"
    )

    val videoPresets = listOf(
        VideoPreset(
            title = "Bhil Bamboo Splitting & Wicker Weaving",
            hindiTitle = "बांसवाड़ा बांस छिलाई व टोकरी बुनाई",
            durationSeconds = 48,
            url = "https://padharo.rajasthan.gov.in/reels/banswara_bamboo_shg.mp4",
            craftOriginSuggestion = "Ancestral Bhil tribal wicker & bamboo craft passed down through 4 generations along the Mahi river basin."
        ),
        VideoPreset(
            title = "Jaipur Blue Pottery Wheel Throwing & Hand-Glaze",
            hindiTitle = "जयपुर ब्लू पॉटरी चाक निर्माण व प्राकृतिक लेप",
            durationSeconds = 55,
            url = "https://padharo.rajasthan.gov.in/reels/jaipur_blue_pottery.mp4",
            craftOriginSuggestion = "150-year royal court quartz glazed blue pottery tradition tracing back to Maharaja Sawai Ram Singh II."
        ),
        VideoPreset(
            title = "Barmer 14-Stage Ajrakh Indigo Block Printing",
            hindiTitle = "बाड़मेर अजरक प्राकृतिक नील व मजीठ छपाई",
            durationSeconds = 62,
            url = "https://padharo.rajasthan.gov.in/reels/barmer_ajrakh_printing.mp4",
            craftOriginSuggestion = "Over 400-year-old Sindhi-Marwari double-sided natural resist block printing using river water and indigo roots."
        ),
        VideoPreset(
            title = "Pokhran Golden Terracotta Wheel Throwing",
            hindiTitle = "पोकरण मटका निर्माण व पारंपरिक भट्टी पकाई",
            durationSeconds = 39,
            url = "https://padharo.rajasthan.gov.in/reels/pokhran_terracotta.mp4",
            craftOriginSuggestion = "Mineral-rich red desert terracotta pottery of Pokhran, naturally cooling water for desert travelers for centuries."
        ),
        VideoPreset(
            title = "Jodhpur Sheesham Wood Jharokha Carving",
            hindiTitle = "जोधपुर शीशम झरोखा नक्काशी व हस्तशिल्प",
            durationSeconds = 50,
            url = "https://padharo.rajasthan.gov.in/reels/jodhpur_wood_jharokha.mp4",
            craftOriginSuggestion = "Jodhpur royal palace architectural wood carving tradition using sustainably salvaged Sheesham timber."
        )
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = if (currentLanguage == "HI") "कारीगर की कहानी व वीडियो" else "Meet the Maker Story & Video",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = if (currentLanguage == "HI") "उत्पाद पृष्ठ पर प्रमाणिकता प्रदर्शित करें" else "Showcase authenticity on your product pages",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_edit_maker_dialog_btn")) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        },
                        actions = {
                            TextButton(
                                onClick = { showPreview = !showPreview },
                                modifier = Modifier.testTag("toggle_maker_preview_btn")
                            ) {
                                Icon(
                                    imageVector = if (showPreview) Icons.Default.Edit else Icons.Default.Visibility,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (showPreview) (if (currentLanguage == "HI") "संपादित करें" else "Edit") else (if (currentLanguage == "HI") "पूर्वावलोकन" else "Preview"))
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                },
                bottomBar = {
                    Surface(
                        tonalElevation = 8.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(if (currentLanguage == "HI") "रद्द करें" else "Cancel")
                            }

                            Button(
                                onClick = {
                                    onSave(
                                        artisanName,
                                        businessName,
                                        district,
                                        villageOrCity,
                                        fullAddress,
                                        craftOrigin,
                                        artisanStory,
                                        videoUrl,
                                        videoTitle,
                                        videoDurationSeconds
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("save_maker_story_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (currentLanguage == "HI") "सहेजें व प्रकाशित करें" else "Save & Publish", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            ) { padding ->
                if (showPreview) {
                    val previewSeller = seller.copy(
                        artisanName = artisanName,
                        businessName = businessName,
                        district = district,
                        villageOrCity = villageOrCity,
                        fullAddress = fullAddress,
                        craftOrigin = craftOrigin,
                        artisanStory = artisanStory,
                        videoUrl = videoUrl,
                        videoTitle = videoTitle,
                        videoDurationSeconds = videoDurationSeconds
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            color = RoyalSaffron.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = null, tint = RoyalSaffron)
                                Text(
                                    text = if (currentLanguage == "HI") "यह कार्ड आपके सभी उत्पाद पृष्ठों पर ग्राहकों को दिखेगा:" else "Live preview: This card will appear on all your product pages:",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = RoyalSaffron
                                )
                            }
                        }

                        MeetTheMakerSection(
                            seller = previewSeller,
                            currentLanguage = currentLanguage
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Section: Identity & Location
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = if (currentLanguage == "HI") "कारीगर व कार्यशाला पहचान" else "Artisan & Workshop Identity",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = RoyalIndigo
                                )

                                OutlinedTextField(
                                    value = artisanName,
                                    onValueChange = { artisanName = it },
                                    label = { Text(if (currentLanguage == "HI") "मुख्य कारीगर / शिल्पकार का नाम" else "Master Artisan Name") },
                                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("maker_name_input"),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = businessName,
                                    onValueChange = { businessName = it },
                                    label = { Text(if (currentLanguage == "HI") "व्यापार / स्वयं सहायता समूह (SHG) नाम" else "Business / SHG Name") },
                                    leadingIcon = { Icon(Icons.Default.Store, contentDescription = null) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("maker_business_input"),
                                    singleLine = true
                                )

                                // District Selector
                                ExposedDropdownMenuBox(
                                    expanded = isDistrictDropdownOpen,
                                    onExpandedChange = { isDistrictDropdownOpen = it }
                                ) {
                                    OutlinedTextField(
                                        value = district,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text(if (currentLanguage == "HI") "राजस्थान ज़िला" else "Rajasthan District") },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDistrictDropdownOpen) },
                                        modifier = Modifier
                                            .menuAnchor()
                                            .fillMaxWidth()
                                            .testTag("maker_district_selector")
                                    )
                                    ExposedDropdownMenu(
                                        expanded = isDistrictDropdownOpen,
                                        onDismissRequest = { isDistrictDropdownOpen = false }
                                    ) {
                                        districts.forEach { dist ->
                                            DropdownMenuItem(
                                                text = { Text(dist) },
                                                onClick = {
                                                    district = dist
                                                    isDistrictDropdownOpen = false
                                                }
                                            )
                                        }
                                    }
                                }

                                OutlinedTextField(
                                    value = villageOrCity,
                                    onValueChange = { villageOrCity = it },
                                    label = { Text(if (currentLanguage == "HI") "गाँव / कस्बा / शिल्प क्लस्टर" else "Village / Town / Craft Cluster") },
                                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = fullAddress,
                                    onValueChange = { fullAddress = it },
                                    label = { Text(if (currentLanguage == "HI") "कार्यशाला का पूरा पता" else "Full Workshop Address") },
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 2
                                )
                            }
                        }

                        // Section: Craft Origin & Heritage Story
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = if (currentLanguage == "HI") "शिल्प उद्भव व विरासत (Craft Origin)" else "Craft Origin & Lineage",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = RoyalSaffron
                                )

                                Text(
                                    text = if (currentLanguage == "HI") "बताएं कि यह शिल्प परंपरा कितनी पुरानी है, किस तकनीक और प्राकृतिक सामग्री से बनती है:" 
                                    else "Explain your ancestral lineage, tradition origins, and natural desert materials used:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                OutlinedTextField(
                                    value = craftOrigin,
                                    onValueChange = { craftOrigin = it },
                                    label = { Text(if (currentLanguage == "HI") "शिल्प उद्भव व पैतृक परंपरा" else "Craft Origin & Ancestral Technique") },
                                    placeholder = { Text("e.g. 4th-generation tribal bamboo craft passed down through women elders...") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                        .testTag("maker_craft_origin_input"),
                                    maxLines = 4
                                )

                                OutlinedTextField(
                                    value = artisanStory,
                                    onValueChange = { artisanStory = it },
                                    label = { Text(if (currentLanguage == "HI") "विस्तृत व्यक्तिगत कहानी व उद्देश्य" else "Detailed Personal Story & Mission") },
                                    placeholder = { Text("Tell customers about yourself, your family, how you make products...") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .testTag("maker_story_input"),
                                    maxLines = 6
                                )
                            }
                        }

                        // Section: Short Video Upload / Selection
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Videocam, contentDescription = null, tint = RoyalSaffron)
                                    Text(
                                        text = if (currentLanguage == "HI") "कार्यशाला शॉर्ट वीडियो (रील्स)" else "Short Workshop Video Reel",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }

                                Text(
                                    text = if (currentLanguage == "HI") "ग्राहक हाथ से बनते हुए उत्पाद का वीडियो देखकर 3 गुना अधिक विश्वास करते हैं। नीचे एक तैयार वीडियो चुनें या अपना वीडियो URL डालें:"
                                    else "Shoppers trust handmade crafts 3x more when they see the maker in action. Select an authentic workshop reel or enter your custom video:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                // Preset Reels Selector
                                Text(
                                    text = if (currentLanguage == "HI") "प्रमाणित कार्यशाला रील्स टेम्पलेट (1-टैप चयन):" else "Certified Workshop Reel Presets (1-tap select):",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = RoyalIndigo
                                )

                                videoPresets.forEach { preset ->
                                    val isSelected = videoUrl == preset.url
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .border(
                                                width = if (isSelected) 2.dp else 1.dp,
                                                color = if (isSelected) RoyalSaffron else MaterialTheme.colorScheme.outlineVariant,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .clickable {
                                                videoUrl = preset.url
                                                videoTitle = if (currentLanguage == "HI") preset.hindiTitle else preset.title
                                                videoDurationSeconds = preset.durationSeconds
                                                if (craftOrigin.isBlank() || craftOrigin.contains("Ancestral handmade craft")) {
                                                    craftOrigin = preset.craftOriginSuggestion
                                                }
                                            },
                                        color = if (isSelected) RoyalSaffron.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Icon(
                                                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.PlayCircle,
                                                    contentDescription = null,
                                                    tint = if (isSelected) RoyalSaffron else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Column {
                                                    Text(
                                                        text = if (currentLanguage == "HI") preset.hindiTitle else preset.title,
                                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                                    )
                                                    Text(
                                                        text = "${preset.durationSeconds}s Workshop Reel • HD 1080p",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                                OutlinedTextField(
                                    value = videoUrl,
                                    onValueChange = { videoUrl = it },
                                    label = { Text(if (currentLanguage == "HI") "कस्टम वीडियो URL / फाइल लिंक" else "Custom Video URL / Reel Link") },
                                    placeholder = { Text("https://... or content://media/...") },
                                    leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("maker_video_url_input"),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = videoTitle,
                                    onValueChange = { videoTitle = it },
                                    label = { Text(if (currentLanguage == "HI") "वीडियो का शीर्षक" else "Video Title / Caption") },
                                    leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("maker_video_title_input"),
                                    singleLine = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
