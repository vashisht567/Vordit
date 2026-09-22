package com.example.ui.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
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
import com.example.ui.components.ProductCard
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron

data class DistrictInfo(
    val name: String,
    val hindiName: String,
    val specialty: String,
    val hindiSpecialty: String,
    val description: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreRajasthanScreen(
    products: List<ProductEntity>,
    currentLanguage: String,
    selectedDistrict: String?,
    wishlistProductIds: Set<String>,
    onSelectDistrict: (String?) -> Unit,
    onProductClick: (ProductEntity) -> Unit,
    onAddToCartClick: (ProductEntity) -> Unit,
    onWishlistToggle: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val districts = remember {
        listOf(
            DistrictInfo(
                "Banswara",
                "बांसवाड़ा",
                "Tribal Bamboo & Cane Crafts",
                "जनजातीय प्राकृतिक बांस एवं बेंत शिल्प",
                "Forested hills where Bhil tribal artisan SHGs hand-weave durable baskets and lamps.",
                RoyalSaffron
            ),
            DistrictInfo(
                "Jaipur",
                "जयपुर",
                "Blue Pottery, Lac Bangles & Sanganeri Print",
                "ब्लू पॉटरी, लाख की चूड़ियां व सांगानेरी ब्लॉक प्रिंट",
                "The historic craft capital celebrated for quartz glazed ceramics and royal block textiles.",
                RoyalIndigo
            ),
            DistrictInfo(
                "Barmer",
                "बाड़मेर",
                "Ajrakh Natural Indigo Block Prints",
                "शुद्ध इंडिगो अजरख ब्लॉक प्रिंट व कशीदाकारी",
                "Thar desert craft tradition utilizing celestial geometric motifs and organic plant dyes.",
                Color(0xFF2E7D32)
            ),
            DistrictInfo(
                "Jodhpur",
                "जोधपुर",
                "Sheesham Wood Jharokhas & Vintage Craft",
                "शीशम लकड़ी के झरोखे, फर्नीचर व मोजड़ी",
                "Master woodcarvers chiseled after Mehrangarh palace balustrades and heritage timber.",
                Color(0xFF6D4C41)
            ),
            DistrictInfo(
                "Jaisalmer",
                "जैसलमेर / पोकरण",
                "Pokhran Desert Terracotta & Clay Pots",
                "पोकरण की लाल मिट्टी के प्राकृतिक शीतल मटके",
                "Mineral-rich golden desert clay fired into natural water cooling vessels.",
                Color(0xFFD84315)
            ),
            DistrictInfo(
                "Udaipur",
                "उदयपुर",
                "Pichwai Silk Art & Miniature Paintings",
                "पिछवाई पेंटिंग, कामधेनु कला व काष्ठ कला",
                "Temple art of Mewar hand-painted using natural stone pigments and pure gold leaf.",
                Color(0xFF5E35B1)
            ),
            DistrictInfo(
                "Kota",
                "कोटा",
                "Kota Doria Pit-Loom Zari Sarees",
                "कोटा डोरिया हथकरघा खाट-बुनाई जरी साड़ियां",
                "Delicate checkered translucent handloom fabric woven for centuries in Kaithun cluster.",
                Color(0xFF00897B)
            ),
            DistrictInfo(
                "Dungarpur",
                "डूंगरपुर",
                "Vagad Herbal Honey & Organic Forest Produce",
                "वागड़ का शुद्ध जंगली शहद, आंवला व औषधियां",
                "Sustainably harvested raw multi-flora forest products by rural tribal cooperatives.",
                Color(0xFFF57F17)
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("explore_rajasthan_screen"),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (selectedDistrict != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onSelectDistrict(null) }
                            .padding(vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = RoyalSaffron
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentLanguage == "HI") "सभी ज़िले देखें" else "All Rajasthan Districts",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = RoyalSaffron
                        )
                    }
                }

                Text(
                    text = if (selectedDistrict != null) {
                        if (currentLanguage == "HI") "ज़िला: $selectedDistrict की कला" else "District: $selectedDistrict Heritage"
                    } else {
                        if (currentLanguage == "HI") "राजस्थान: ज़िलों की कला और पहचान" else "Explore Rajasthan by District & Craft"
                    },
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black)
                )

                Text(
                    text = if (selectedDistrict != null) {
                        if (currentLanguage == "HI") "$selectedDistrict के प्रमाणित कारीगरों द्वारा बनाए गए प्रामाणिक उत्पाद।" else "Authentic creations verified directly from artisans in $selectedDistrict."
                    } else {
                        if (currentLanguage == "HI") "हर ज़िले का अपना अनूठा शिल्प है। ज़िला चुनकर स्थानीय उत्पादों का अन्वेषण करें।" else "Each region holds an ancient craft lineage. Choose a district to discover local artisans directly."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // District Cards (shown when no district is selected)
        if (selectedDistrict == null) {
            items(districts) { dist ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { onSelectDistrict(dist.name) }
                        .testTag("explore_district_card_${dist.name}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(dist.color.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = dist.color,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (currentLanguage == "HI") dist.hindiName else dist.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "(${dist.name})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = if (currentLanguage == "HI") dist.hindiSpecialty else dist.specialty,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = dist.color
                            )
                            Text(
                                text = dist.description,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Explore",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            // Filtered Products for selected district
            val districtProducts = products.filter { it.district.equals(selectedDistrict, ignoreCase = true) }

            if (districtProducts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (currentLanguage == "HI") "इस ज़िले के उत्पाद जल्द जोड़े जा रहे हैं।" else "Products from this district are being added soon.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                val chunked = districtProducts.chunked(2)
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
}
