package com.example.ui.screens.seller

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
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
import com.example.data.local.entities.OrderEntity
import com.example.data.local.entities.ProductEntity
import com.example.data.local.entities.SellerProfileEntity
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.SellerStatusBadge
import com.example.ui.theme.OasisGreen
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron
import com.example.ui.theme.StarGold

@Composable
fun SellerDashboardScreen(
    seller: SellerProfileEntity?,
    products: List<ProductEntity>,
    orders: List<OrderEntity>,
    currentLanguage: String,
    onAddProductClick: () -> Unit,
    onDeleteProduct: (String) -> Unit,
    onUpdateOrderStatus: (String, String) -> Unit,
    onUpdateMakerProfile: (
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
    ) -> Unit = { _, _, _, _, _, _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    var showEditMakerDialog by remember { mutableStateOf(false) }

    val myProducts = products.filter { it.sellerId == (seller?.id ?: "sel_1") }
    val totalRevenue = orders.filter { it.orderStatus != "CANCELLED" }.sumOf { it.totalAmount }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("seller_dashboard_scrollable"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Seller Identity Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = seller?.businessName ?: "Banswara Tribal Bamboo SHG",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = OasisGreen, modifier = Modifier.size(18.dp))
                                }
                                Text(
                                    text = "Artisan: ${seller?.artisanName ?: "Shanti Devi"} • ${seller?.district ?: "Banswara"}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            SellerStatusBadge(status = seller?.status ?: "APPROVED")
                        }

                        Text(
                            text = seller?.artisanStory ?: "Direct tribal artisan cooperative empowering 25 women weavers.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Prominent "Meet the Maker & Workshop Story" Management Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("seller_meet_the_maker_mgmt_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RoyalSaffron.copy(alpha = 0.08f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = RoyalSaffron)
                                Text(
                                    text = if (currentLanguage == "HI") "'कारीगर से मिलें' प्रोफ़ाइल व वीडियो" else "Meet the Maker & Video Story",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Surface(
                                color = if (seller?.status == "APPROVED") OasisGreen.copy(alpha = 0.15f) else Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (seller?.status == "APPROVED") {
                                        if (currentLanguage == "HI") "उत्पाद पृष्ठों पर सक्रिय" else "Live on Products"
                                    } else {
                                        if (currentLanguage == "HI") "सत्यापन लंबित" else "Pending Review"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (seller?.status == "APPROVED") OasisGreen else RoyalSaffron,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Text(
                            text = if (currentLanguage == "HI") "आपकी प्रामाणिक कहानी, शिल्प विरासत व कार्यशाला वीडियो सीधे आपके उत्पाद पृष्ठों पर प्रदर्शित होती है।"
                            else "Your personal craft story, ancestral heritage, and workshop video reel are prominently displayed on all your product pages to build customer trust.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Craft Origin Summary
                        if (!seller?.craftOrigin.isNullOrBlank()) {
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.HistoryEdu, contentDescription = null, tint = RoyalSaffron, modifier = Modifier.size(16.dp))
                                    Column {
                                        Text(
                                            text = if (currentLanguage == "HI") "शिल्प उद्भव:" else "Craft Origin:",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = RoyalSaffron
                                        )
                                        Text(
                                            text = seller!!.craftOrigin,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 2
                                        )
                                    }
                                }
                            }
                        }

                        // Video Reel Status
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = null,
                                    tint = if (!seller?.videoUrl.isNullOrBlank()) RoyalIndigo else MaterialTheme.colorScheme.outline
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (!seller?.videoUrl.isNullOrBlank()) {
                                            seller?.videoTitle?.ifBlank { "Workshop Video Reel" } ?: "Workshop Video Reel"
                                        } else {
                                            if (currentLanguage == "HI") "कोई वीडियो नहीं जोड़ा गया" else "No short video uploaded yet"
                                        },
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Text(
                                        text = if (!seller?.videoUrl.isNullOrBlank()) {
                                            "${seller?.videoDurationSeconds ?: 45}s • HD Reel Verified"
                                        } else {
                                            if (currentLanguage == "HI") "ग्राहकों का विश्वास बढ़ाने के लिए वीडियो जोड़ें" else "Add a 30-60s reel to increase buyer trust"
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Edit / Add Button
                        Button(
                            onClick = { showEditMakerDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalIndigo),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("open_edit_maker_dialog_btn")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLanguage == "HI") "कहानी, स्थान व वीडियो संपादित करें" else "Edit Maker Story, Location & Video",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

        // Metrics Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = RoyalSaffron.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = RoyalSaffron, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "₹${totalRevenue.toInt()}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = RoyalSaffron
                        )
                        Text(
                            text = if (currentLanguage == "HI") "कुल बिक्री" else "Total Revenue",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = RoyalIndigo.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.Inventory, contentDescription = null, tint = RoyalIndigo, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${myProducts.size}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = RoyalIndigo
                        )
                        Text(
                            text = if (currentLanguage == "HI") "सूचीबद्ध उत्पाद" else "Listed Crafts",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = StarGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${seller?.rating ?: 4.9} ★",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = OasisGreen
                        )
                        Text(
                            text = if (currentLanguage == "HI") "कारीगर रेटिंग" else "Artisan Rating",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Action: Add Product with Gemini AI
        item {
            Button(
                onClick = onAddProductClick,
                colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("seller_add_product_nav_btn")
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (currentLanguage == "HI") "नया उत्पाद जोड़ें (AI सहायक)" else "Add New Product with Gemini AI",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // My Products Section
        item {
            Text(
                text = if (currentLanguage == "HI") "मेरे उत्पाद (${myProducts.size})" else "My Products (${myProducts.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        items(myProducts) { prod ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(RoyalSaffron.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = prod.category.take(2).uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = RoyalSaffron
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (currentLanguage == "HI" && prod.hindiName.isNotBlank()) prod.hindiName else prod.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1
                        )
                        Text(
                            text = "₹${prod.price.toInt()} • Stock: ${prod.stock}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { onDeleteProduct(prod.id) },
                        modifier = Modifier.testTag("delete_product_${prod.id}")
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        // Incoming Orders for this Seller
        item {
            Text(
                text = if (currentLanguage == "HI") "आगामी ग्राहक ऑर्डर्स" else "Incoming Customer Orders",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (orders.isEmpty()) {
            item {
                Text(
                    text = if (currentLanguage == "HI") "कोई नया ऑर्डर लंबित नहीं है।" else "No pending orders at this moment.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(orders.take(3)) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = order.id, fontWeight = FontWeight.Bold, color = RoyalSaffron)
                            OrderStatusBadge(status = order.orderStatus)
                        }

                        Text(
                            text = "Customer: ${order.customerName} (${order.customerPhone})",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Deliver to: ${order.deliveryAddress}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Action buttons for status progression
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (order.orderStatus == "PLACED" || order.orderStatus == "PROCESSING") {
                                OutlinedButton(
                                    onClick = { onUpdateOrderStatus(order.id, "PACKED") },
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text("Mark Packed")
                                }
                            } else if (order.orderStatus == "PACKED") {
                                Button(
                                    onClick = { onUpdateOrderStatus(order.id, "SHIPPED") },
                                    colors = ButtonDefaults.buttonColors(containerColor = RoyalIndigo),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Ship with Courier")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showEditMakerDialog && seller != null) {
        EditMakerStoryDialog(
            seller = seller,
            currentLanguage = currentLanguage,
            onDismiss = { showEditMakerDialog = false },
            onSave = { artisanName, businessName, district, villageOrCity, fullAddress, craftOrigin, artisanStory, videoUrl, videoTitle, videoDurationSeconds ->
                onUpdateMakerProfile(
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
                showEditMakerDialog = false
            }
        )
    }
}
}
