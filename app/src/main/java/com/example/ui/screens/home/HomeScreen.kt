package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.ProductEntity
import com.example.ui.components.CategorySelectorRow
import com.example.ui.components.DistrictSelectorRow
import com.example.ui.components.ProductCard
import com.example.ui.theme.OasisGreen
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron
import com.example.ui.theme.SandstoneGold

@Composable
fun HomeScreen(
    products: List<ProductEntity>,
    featuredProducts: List<ProductEntity>,
    currentLanguage: String,
    searchQuery: String,
    selectedCategory: String?,
    selectedDistrict: String?,
    wishlistProductIds: Set<String>,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String?) -> Unit,
    onDistrictSelect: (String?) -> Unit,
    onProductClick: (ProductEntity) -> Unit,
    onAddToCartClick: (ProductEntity) -> Unit,
    onWishlistToggle: (ProductEntity) -> Unit,
    onExploreDistrictsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = remember {
        listOf(
            "Handicrafts & Decor",
            "Blue Pottery",
            "Textiles & Bandhej",
            "Terracotta & Clay",
            "Wood Carvings",
            "Food & Spices",
            "Lac Bangles & Jewelry"
        )
    }

    val districts = remember {
        listOf("Jaipur", "Jodhpur", "Udaipur", "Bikaner", "Kota", "Jaisalmer", "Banswara", "Barmer", "Dungarpur")
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_content"),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("home_search_input"),
                placeholder = {
                    Text(
                        if (currentLanguage == "HI") "कला, शिल्प, ज़िला या कारीगर खोजें..." else "Search crafts, blue pottery, district, artisan..."
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = RoyalSaffron
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoyalSaffron,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }

        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                RoyalSaffron,
                                Color(0xFFE65100),
                                RoyalIndigo
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentLanguage == "HI") "100% प्रामाणिक राजस्थानी हस्तशिल्प" else "100% Authentic Rajasthani Craft",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (currentLanguage == "HI") "सीधे गाँव के कारीगरों और स्वयं सहायता समूहों से" else "Direct from Village Artisans & Tribal SHGs",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        ),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (currentLanguage == "HI") "बिना बिचौलियों के उचित दाम। आपकी खरीद से कारीगरों के परिवारों को सीधी आर्थिक मदद मिलती है।" else "Fair prices, zero middlemen. Every purchase directly empowers rural families and preserves cultural heritage.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onExploreDistrictsClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = RoyalSaffron
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("hero_explore_districts_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentLanguage == "HI") "ज़िलेवार कला खोजें" else "Explore by District",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Category Filter Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PaddingValues(horizontal = 16.dp).let {
                    Text(
                        text = if (currentLanguage == "HI") "शिल्प श्रेणियाँ" else "Craft Categories",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                CategorySelectorRow(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onSelectCategory = onCategorySelect
                )
            }
        }

        // District Quick Filter
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (currentLanguage == "HI") "राजस्थान के ज़िले" else "Artisans by District",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (currentLanguage == "HI") "सभी देखें" else "View All",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = RoyalSaffron,
                        modifier = Modifier.clickable { onExploreDistrictsClick() }
                    )
                }
                DistrictSelectorRow(
                    districts = districts,
                    selectedDistrict = selectedDistrict,
                    onSelectDistrict = onDistrictSelect
                )
            }
        }

        // Artisan Spotlight Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(RoyalSaffron.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "श",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = RoyalSaffron
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "शांति देवी (Shanti Devi)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = OasisGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = if (currentLanguage == "HI") "बांसवाड़ा जनजातीय महिला बांस शिल्प समूह" else "Banswara Tribal Women Bamboo SHG",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (currentLanguage == "HI") "25 महिला कारीगरों का समूह • सीधे वागड़ के जंगलों से" else "Group of 25 tribal artisans • Direct forest bamboo",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Section Title: Products
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedDistrict != null) {
                        if (currentLanguage == "HI") "$selectedDistrict के उत्पाद" else "Products from $selectedDistrict"
                    } else if (selectedCategory != null) {
                        selectedCategory
                    } else {
                        if (currentLanguage == "HI") "लोकप्रिय हस्तशिल्प और उत्पाद" else "Popular Handicrafts & Specialties"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${products.size} Items",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Products Grid (2 columns representation)
        if (products.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (currentLanguage == "HI") "कोई उत्पाद नहीं मिला। कृपया अन्य फ़िल्टर चुनें।" else "No products found. Please try another filter.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            val chunked = products.chunked(2)
            items(chunked) { rowProducts ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (product in rowProducts) {
                        Box(modifier = Modifier.weight(1f)) {
                            ProductCard(
                                product = product,
                                currentLanguage = currentLanguage,
                                isInWishlist = wishlistProductIds.contains(product.id),
                                onProductClick = onProductClick,
                                onAddToCartClick = onAddToCartClick,
                                onWishlistToggle = onWishlistToggle
                            )
                        }
                    }
                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
