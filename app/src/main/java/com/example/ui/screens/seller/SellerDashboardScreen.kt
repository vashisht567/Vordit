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
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Verified
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
    modifier: Modifier = Modifier
) {
    val myProducts = products.filter { it.sellerId == (seller?.id ?: "sel_1") }
    val totalRevenue = orders.filter { it.orderStatus != "CANCELLED" }.sumOf { it.totalAmount }

    LazyColumn(
        modifier = modifier
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
}
