package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.VerifiedUser
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
import com.example.data.local.entities.PlatformSettingsEntity
import com.example.data.local.entities.SellerProfileEntity
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.SellerStatusBadge
import com.example.ui.theme.OasisGreen
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron

@Composable
fun AdminPanelScreen(
    currentLanguage: String,
    allSellers: List<SellerProfileEntity>,
    pendingSellers: List<SellerProfileEntity>,
    allOrders: List<OrderEntity>,
    settings: PlatformSettingsEntity?,
    onApproveSeller: (String) -> Unit,
    onRejectSeller: (String) -> Unit,
    onUpdateOrderStatus: (String, String) -> Unit,
    onUpdateCommission: (Double) -> Unit,
    onClearDemoData: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showClearDemoDialog by remember { mutableStateOf(false) }
    val commissionPercent = settings?.commissionPercentage ?: 8.0

    val totalGMV = allOrders.filter { it.orderStatus != "CANCELLED" }.sumOf { it.totalAmount }
    val platformEarnings = totalGMV * (commissionPercent / 100.0)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_panel_scrollable"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RoyalIndigo.copy(alpha = 0.08f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = RoyalIndigo,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (currentLanguage == "HI") "पधारो राजस्थान - प्रशासनिक नियंत्रण" else "Padharo Rajasthan Admin Console",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = RoyalIndigo
                        )
                        Text(
                            text = if (currentLanguage == "HI") "कारीगर सत्यापन, बाज़ार GMV एवं ऑर्डर निगरानी" else "Artisan verifications, platform GMV and order compliance",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Metrics Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = RoyalSaffron, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "₹${totalGMV.toInt()}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black))
                        Text(text = "Total GMV", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.Percent, contentDescription = null, tint = OasisGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "₹${platformEarnings.toInt()}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = OasisGreen)
                        Text(text = "Commission ($commissionPercent%)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.Group, contentDescription = null, tint = RoyalIndigo, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${allSellers.size}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = RoyalIndigo)
                        Text(text = "Total Artisans", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Artisan Verification Queue
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (currentLanguage == "HI") "कारीगर सत्यापन कतार (${pendingSellers.size})" else "Artisan Verification Queue (${pendingSellers.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        if (pendingSellers.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = OasisGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == "HI") "सभी कारीगर प्रोफाइल सत्यापित हैं! कोई लंबित नहीं है।" else "All artisan profiles verified! Zero pending verifications.",
                            style = MaterialTheme.typography.bodySmall,
                            color = OasisGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            items(pendingSellers) { seller ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = seller.businessName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                Text(
                                    text = "Artisan: ${seller.artisanName} • 📍 ${seller.district} (${seller.villageOrCity})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            SellerStatusBadge(status = seller.status)
                        }

                        Text(
                            text = seller.businessDescription,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "Story: ${seller.artisanStory}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onApproveSeller(seller.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = OasisGreen),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("approve_seller_${seller.id}")
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Approve & Verify")
                            }

                            OutlinedButton(
                                onClick = { onRejectSeller(seller.id) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reject")
                            }
                        }
                    }
                }
            }
        }

        // Platform Orders
        item {
            Text(
                text = if (currentLanguage == "HI") "हाल के प्लेटफ़ॉर्म ऑर्डर्स" else "Recent Platform Orders (${allOrders.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(allOrders.take(5)) { order ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = order.id, fontWeight = FontWeight.Bold, color = RoyalSaffron)
                        Text(text = "${order.customerName} • ₹${order.totalAmount.toInt()}", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Track: ${order.trackingNumber}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    OrderStatusBadge(status = order.orderStatus)
                }
            }
        }

        // Platform Governance & Settings
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = if (currentLanguage == "HI") "प्लेटफ़ॉर्म नीतियां एवं नियंत्रण" else "Platform Governance & Demo Controls",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (currentLanguage == "HI") "मार्केटप्लेस कमीशन: $commissionPercent%" else "Artisan Marketplace Commission: $commissionPercent%",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(5.0, 8.0, 10.0, 12.0).forEach { rate ->
                            FilterChip(
                                selected = commissionPercent == rate,
                                onClick = { onUpdateCommission(rate) },
                                label = { Text("$rate%") }
                            )
                        }
                    }

                    HorizontalDivider()

                    Button(
                        onClick = { showClearDemoDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.testTag("clear_demo_data_btn")
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (currentLanguage == "HI") "डेमो उत्पाद हटाएं (Clear Demo Data)" else "Clear Demo Products")
                    }
                }
            }
        }
    }

    if (showClearDemoDialog) {
        AlertDialog(
            onDismissRequest = { showClearDemoDialog = false },
            title = { Text("Clear Demo Products?") },
            text = { Text("This will permanently remove the pre-seeded demo crafts from the database so you can test production seller onboarding cleanly.") },
            confirmButton = {
                Button(
                    onClick = {
                        onClearDemoData()
                        showClearDemoDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Yes, Clear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDemoDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
