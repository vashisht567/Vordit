package com.example.ui.screens.seller

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.ProductEntity
import com.example.ui.theme.OasisGreen
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron
import com.example.ui.viewmodel.AiGenerationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    currentLanguage: String,
    sellerDistrict: String,
    sellerId: String,
    sellerName: String,
    aiState: AiGenerationState,
    onBackClick: () -> Unit,
    onGenerateAi: (String, String) -> Unit,
    onResetAi: () -> Unit,
    onPublishProduct: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var rawInputPrompt by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("") }
    var hindiName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Handicrafts & Decor") }
    var priceText by remember { mutableStateOf("450") }
    var stockText by remember { mutableStateOf("10") }
    var discountText by remember { mutableStateOf("10") }
    var shortDesc by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var hindiDesc by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var whatsappCopy by remember { mutableStateOf("") }

    // When AI succeeds, auto-fill fields
    LaunchedEffect(aiState) {
        if (aiState is AiGenerationState.Success) {
            val res = aiState.listing
            name = res.title
            hindiName = res.hindiTitle
            category = res.category
            priceText = res.suggestedPrice.toInt().toString()
            shortDesc = res.shortDescription
            desc = res.englishDescription
            hindiDesc = res.hindiDescription
            tags = res.tags
            whatsappCopy = res.whatsappCaption
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (currentLanguage == "HI") "नया उत्पाद जोड़ें (AI सहायक)" else "Add Craft (AI Assistant)") },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("add_product_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = {
                        if (name.isNotBlank() && priceText.toDoubleOrNull() != null) {
                            val prod = ProductEntity(
                                id = "prod_" + System.currentTimeMillis().toString().takeLast(6),
                                sellerId = sellerId,
                                name = name,
                                hindiName = hindiName,
                                category = category,
                                description = desc,
                                hindiDescription = hindiDesc,
                                shortDescription = shortDesc,
                                price = priceText.toDoubleOrNull() ?: 450.0,
                                discountPercent = discountText.toIntOrNull() ?: 0,
                                stock = stockText.toIntOrNull() ?: 10,
                                sku = "RJ-" + category.take(3).uppercase() + "-" + (100..999).random(),
                                imageUrl = "craft_item",
                                district = sellerDistrict,
                                makerName = sellerName,
                                makerStory = "Authentic handcrafted piece made in $sellerDistrict, Rajasthan.",
                                tags = tags,
                                isApproved = true,
                                isDemo = false
                            )
                            onPublishProduct(prod)
                            onResetAi()
                        }
                    },
                    enabled = name.isNotBlank() && priceText.toDoubleOrNull() != null,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("publish_product_btn")
                ) {
                    Icon(Icons.Default.Publish, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentLanguage == "HI") "मार्केटप्लेस पर प्रकाशित करें" else "Publish to Marketplace",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        },
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("add_product_scrollable"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // AI Seller Assistant Banner Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = RoyalSaffron.copy(alpha = 0.08f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = RoyalSaffron)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (currentLanguage == "HI") "AI कारीगर सहायक (Gemini)" else "AI Seller Assistant (Gemini)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = RoyalSaffron
                            )
                        }

                        Text(
                            text = if (currentLanguage == "HI") "अपने उत्पाद का विवरण हिंदी या अंग्रेजी में बोलें या लिखें। AI अपने आप द्विभाषी शीर्षक, विवरण, मूल्य और व्हाट्सएप संदेश बना देगा!" else "Type or describe your craft in simple Hindi/English. Gemini AI automatically crafts professional bilingual listings, tags & social copy!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = rawInputPrompt,
                            onValueChange = { rawInputPrompt = it },
                            placeholder = {
                                Text(
                                    if (currentLanguage == "HI") "उदा: यह बांसवाड़ा के हरे बांस से हाथ से बुनी टोकरी है, कीमत 450 रुपये..." else "e.g. This is a handwoven green bamboo basket from Banswara, price 450 rs..."
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .testTag("ai_prompt_input"),
                            maxLines = 3
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (rawInputPrompt.isNotBlank()) {
                                        onGenerateAi(rawInputPrompt, sellerDistrict)
                                    }
                                },
                                enabled = rawInputPrompt.isNotBlank() && aiState !is AiGenerationState.Loading,
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("generate_ai_listing_btn")
                            ) {
                                if (aiState is AiGenerationState.Loading) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Generating...")
                                } else {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (currentLanguage == "HI") "AI से लिस्टिंग बनाएं" else "Generate Listing")
                                }
                            }

                            // Quick sample button
                            OutlinedButton(
                                onClick = {
                                    rawInputPrompt = "यह बांसवाड़ा की शुद्ध प्राकृतिक बांस की हाथ से बुनी टोकरी है, मूल्य 480 रुपये, वजन 400 ग्राम।"
                                }
                            ) {
                                Text("Fill Sample")
                            }
                        }

                        if (aiState is AiGenerationState.Success) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE8F5E9))
                                    .padding(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = OasisGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (currentLanguage == "HI") "AI द्वारा फ़ील्ड भर दिए गए हैं! आप नीचे बदलाव कर सकते हैं।" else "AI filled the listing below! Feel free to review or edit.",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = OasisGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Editable Form Fields
            item {
                Text(
                    text = if (currentLanguage == "HI") "उत्पाद विवरण (संपादनीय)" else "Listing Details (Editable)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Title (English)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("product_name_input"),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = hindiName,
                    onValueChange = { hindiName = it },
                    label = { Text("उत्पाद का नाम (हिंदी)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Price (₹)") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("product_price_input"),
                        singleLine = true
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { stockText = it },
                        label = { Text("Available Stock") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = discountText,
                        onValueChange = { discountText = it },
                        label = { Text("Discount %") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("English Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    maxLines = 4
                )
            }

            item {
                OutlinedTextField(
                    value = hindiDesc,
                    onValueChange = { hindiDesc = it },
                    label = { Text("हिंदी विवरण (Hindi Description)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    maxLines = 4
                )
            }

            item {
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Search Tags (Comma separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // WhatsApp Marketing Copy from AI
            if (whatsappCopy.isNotBlank()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF00796B), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (currentLanguage == "HI") "व्हाट्सएप शेयर संदेश (AI जनरेटेड)" else "WhatsApp Ready Broadcast (AI Generated)",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF00796B)
                                )
                            }
                            Text(text = whatsappCopy, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
