package com.example.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.CartItemEntity
import com.example.data.local.entities.ProductEntity
import com.example.ui.theme.OasisGreen
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<Pair<CartItemEntity, ProductEntity>>,
    currentLanguage: String,
    onBackClick: () -> Unit,
    onUpdateQuantity: (Long, Int) -> Unit,
    onRemoveItem: (Long) -> Unit,
    onProceedToCheckout: () -> Unit,
    onContinueShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subtotal = cartItems.sumOf { (cart, prod) ->
        val price = if (prod.discountPercent > 0) prod.price * (100 - prod.discountPercent) / 100.0 else prod.price
        price * cart.quantity
    }
    val deliveryFee = if (subtotal > 499 || subtotal == 0.0) 0.0 else 49.0
    val total = subtotal + deliveryFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (currentLanguage == "HI") "मेरा कार्ट (${cartItems.size})" else "Shopping Cart (${cartItems.size})") },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("cart_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (currentLanguage == "HI") "कुल देय राशि" else "Total Payable",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "₹${total.toInt()}",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                                    color = RoyalSaffron
                                )
                            }

                            Button(
                                onClick = onProceedToCheckout,
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                                modifier = Modifier.testTag("proceed_checkout_btn")
                            ) {
                                Text(
                                    text = if (currentLanguage == "HI") "ऑर्डर आगे बढ़ाएं" else "Proceed to Buy",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalMall,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(72.dp)
                    )
                    Text(
                        text = if (currentLanguage == "HI") "आपका कार्ट खाली है" else "Your Cart is Empty",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (currentLanguage == "HI") "राजस्थान के प्रामाणिक हस्तशिल्प और उत्पाद खोजें।" else "Discover authentic Rajasthani handicrafts directly from local makers.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = onContinueShopping,
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (currentLanguage == "HI") "शिल्प खोजें" else "Explore Crafts")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .testTag("cart_items_list"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Free Shipping banner
                if (subtotal > 499) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = OasisGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (currentLanguage == "HI") "मुफ्त डिलीवरी लागू (₹499+ के ऑर्डर पर)" else "Free Shipping Applied (Orders above ₹499)",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = OasisGreen
                                )
                            }
                        }
                    }
                }

                // List of items
                items(cartItems) { (cart, prod) ->
                    val unitPrice = if (prod.discountPercent > 0) prod.price * (100 - prod.discountPercent) / 100.0 else prod.price

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(RoyalSaffron.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = prod.district.take(3).uppercase(),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = RoyalSaffron
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (currentLanguage == "HI" && prod.hindiName.isNotBlank()) prod.hindiName else prod.name,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 2
                                )
                                Text(
                                    text = "₹${unitPrice.toInt()} • ${prod.district}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = RoyalSaffron,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedIconButton(
                                        onClick = { onUpdateQuantity(cart.id, cart.quantity - 1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                                    }

                                    Text(
                                        text = cart.quantity.toString(),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    )

                                    OutlinedIconButton(
                                        onClick = { onUpdateQuantity(cart.id, cart.quantity + 1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                                    }
                                }
                            }

                            IconButton(
                                onClick = { onRemoveItem(cart.id) },
                                modifier = Modifier.testTag("remove_cart_item_${cart.id}")
                            ) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }

                // Price Breakdown Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (currentLanguage == "HI") "मूल्य विवरण" else "Price Breakdown",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(if (currentLanguage == "HI") "उप-योग (Subtotal)" else "Subtotal", style = MaterialTheme.typography.bodyMedium)
                                Text("₹${subtotal.toInt()}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(if (currentLanguage == "HI") "डिलीवरी शुल्क" else "Delivery Fee", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    if (deliveryFee == 0.0) "FREE" else "₹${deliveryFee.toInt()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (deliveryFee == 0.0) OasisGreen else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            HorizontalDivider()
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = if (currentLanguage == "HI") "कुल राशि" else "Total Amount",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "₹${total.toInt()}",
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
}
