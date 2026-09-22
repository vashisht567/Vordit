package com.example.ui.screens.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReceiptLong
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
import com.example.ui.components.OrderStatusBadge
import com.example.ui.theme.OasisGreen
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(
    orders: List<OrderEntity>,
    currentLanguage: String,
    onStartShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("orders_screen_list"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = if (currentLanguage == "HI") "मेरे ऑर्डर्स और ट्रैकिंग" else "My Orders & Live Tracking",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black)
                )
                Text(
                    text = if (currentLanguage == "HI") "सीधे स्थानीय कारीगरों से आपके घर तक की यात्रा" else "From village artisan looms to your doorstep",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (orders.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = if (currentLanguage == "HI") "अभी तक कोई ऑर्डर नहीं है" else "No orders placed yet",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Button(
                            onClick = onStartShopping,
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (currentLanguage == "HI") "शिल्प खरीदें" else "Explore Marketplace")
                        }
                    }
                }
            }
        } else {
            items(orders) { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("order_card_${order.id}"),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Order Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = order.id,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
                                    color = RoyalSaffron
                                )
                                Text(
                                    text = dateFormat.format(Date(order.createdAt)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            OrderStatusBadge(status = order.orderStatus)
                        }

                        HorizontalDivider()

                        // Tracking & Delivery Info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = RoyalIndigo, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Track: ${order.trackingNumber}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = RoyalIndigo
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = order.deliveryAddress,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Order Step-by-Step Progress Timeline
                        OrderTimelineStepper(status = order.orderStatus, currentLanguage = currentLanguage)

                        HorizontalDivider()

                        // Total & Payment
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Payment: ${order.paymentMethod}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (order.paymentStatus == "PAID") "✓ Paid Online" else "Cash on Delivery",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (order.paymentStatus == "PAID") OasisGreen else RoyalSaffron
                                )
                            }

                            Text(
                                text = "Total: ₹${order.totalAmount.toInt()}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                color = RoyalSaffron
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderTimelineStepper(
    status: String,
    currentLanguage: String,
    modifier: Modifier = Modifier
) {
    val steps = remember {
        listOf(
            "PLACED" to ("Placed" to "ऑर्डर दिया"),
            "PACKED" to ("Packed" to "पैक हुआ"),
            "SHIPPED" to ("In Transit" to "रवाना हुआ"),
            "DELIVERED" to ("Delivered" to "प्राप्त हुआ")
        )
    }

    val currentStepIndex = when (status) {
        "PLACED", "PAYMENT_CONFIRMED", "PROCESSING" -> 0
        "PACKED" -> 1
        "SHIPPED", "OUT_FOR_DELIVERY" -> 2
        "DELIVERED" -> 3
        else -> 0
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, (stepKey, labels) ->
                val isCompleted = index <= currentStepIndex
                val isCurrent = index == currentStepIndex

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCompleted) RoyalSaffron else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (currentLanguage == "HI") labels.second else labels.first,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (isCompleted) RoyalSaffron else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
