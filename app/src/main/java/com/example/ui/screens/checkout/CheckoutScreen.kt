package com.example.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
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
import com.example.data.local.entities.CartItemEntity
import com.example.data.local.entities.ProductEntity
import com.example.ui.theme.OasisGreen
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartItems: List<Pair<CartItemEntity, ProductEntity>>,
    currentLanguage: String,
    onBackClick: () -> Unit,
    onConfirmOrder: (name: String, phone: String, address: String, district: String, paymentMethod: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var customerName by remember { mutableStateOf("Sunita Sharma") }
    var customerPhone by remember { mutableStateOf("+91 98290 11223") }
    var deliveryAddress by remember { mutableStateOf("Flat 402, Royal Palms, Vaishali Nagar, Jaipur") }
    var district by remember { mutableStateOf("Jaipur") }
    var selectedPaymentMethod by remember { mutableStateOf("UPI / QR Code") }
    var isSubmitting by remember { mutableStateOf(false) }

    val subtotal = cartItems.sumOf { (cart, prod) ->
        val price = if (prod.discountPercent > 0) prod.price * (100 - prod.discountPercent) / 100.0 else prod.price
        price * cart.quantity
    }
    val deliveryFee = if (subtotal > 499) 0.0 else 49.0
    val total = subtotal + deliveryFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (currentLanguage == "HI") "चेकआउट एवं भुगतान" else "Checkout & Payment") },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("checkout_back_btn")) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentLanguage == "HI") "कुल भुगतान" else "Total Payable",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "₹${total.toInt()}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = RoyalSaffron
                        )
                    }

                    Button(
                        onClick = {
                            if (customerName.isNotBlank() && customerPhone.isNotBlank() && deliveryAddress.isNotBlank()) {
                                isSubmitting = true
                                onConfirmOrder(customerName, customerPhone, deliveryAddress, district, selectedPaymentMethod)
                            }
                        },
                        enabled = !isSubmitting && cartItems.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                        modifier = Modifier.testTag("confirm_order_button")
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        } else {
                            Text(
                                text = if (currentLanguage == "HI") "ऑर्डर कन्फर्म करें" else "Confirm & Pay",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("checkout_scrollable"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Shipping Address Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = RoyalSaffron)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (currentLanguage == "HI") "डिलीवरी का पता" else "Delivery Address",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text(if (currentLanguage == "HI") "पूरा नाम" else "Full Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkout_name_input"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = customerPhone,
                            onValueChange = { customerPhone = it },
                            label = { Text(if (currentLanguage == "HI") "मोबाइल नंबर" else "Mobile Number") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkout_phone_input"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = deliveryAddress,
                            onValueChange = { deliveryAddress = it },
                            label = { Text(if (currentLanguage == "HI") "घर का पता / पिन कोड" else "Address, House No, Pincode") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkout_address_input"),
                            maxLines = 3
                        )

                        OutlinedTextField(
                            value = district,
                            onValueChange = { district = it },
                            label = { Text(if (currentLanguage == "HI") "ज़िला (District)" else "District / City") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkout_district_input"),
                            singleLine = true
                        )
                    }
                }
            }

            // Payment Methods Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = RoyalIndigo)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (currentLanguage == "HI") "भुगतान विकल्प" else "Payment Method",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        val paymentOptions = listOf(
                            Triple("UPI / QR Code", "GPay, PhonePe, Paytm, BHIM", Icons.Default.QrCode2),
                            Triple("Cash on Delivery (COD)", "Pay when craft arrives at doorstep", Icons.Default.Payments),
                            Triple("Cards / NetBanking", "Razorpay Secured Gateway", Icons.Default.CreditCard)
                        )

                        paymentOptions.forEach { (title, subtitle, icon) ->
                            val isSelected = selectedPaymentMethod == title
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedPaymentMethod = title }
                                    .testTag("payment_method_${title.take(3)}"),
                                color = if (isSelected) RoyalSaffron.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedPaymentMethod = title },
                                        colors = RadioButtonDefaults.colors(selectedColor = RoyalSaffron)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(icon, contentDescription = null, tint = if (isSelected) RoyalSaffron else MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Trust & Escrow Guarantee
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = OasisGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == "HI") "पधारो सुरक्षा: आपका भुगतान कारीगर को सामान सुरक्षित मिलने के बाद ही रिलीज होता है।" else "Padharo Safe Escrow: Payment is released to artisans only after safe delivery.",
                            style = MaterialTheme.typography.labelSmall,
                            color = OasisGreen
                        )
                    }
                }
            }
        }
    }
}
