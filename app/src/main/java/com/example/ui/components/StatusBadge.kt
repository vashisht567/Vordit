package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OasisGreen
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron
import com.example.ui.theme.SandstoneGold

@Composable
fun OrderStatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        "PLACED" -> Triple(Color(0xFFE3F2FD), Color(0xFF1565C0), "Order Placed / नया ऑर्डर")
        "PAYMENT_CONFIRMED" -> Triple(Color(0xFFE8F5E9), OasisGreen, "Payment Confirmed / भुगतान प्राप्त")
        "PROCESSING" -> Triple(Color(0xFFFFF3E0), RoyalSaffron, "Artisan Preparing / तैयारी में")
        "PACKED" -> Triple(Color(0xFFEDE7F6), RoyalIndigo, "Packed / पैक हुआ")
        "SHIPPED" -> Triple(Color(0xFFE0F7FA), Color(0xFF00838F), "Dispatched / रवाना हुआ")
        "OUT_FOR_DELIVERY" -> Triple(Color(0xFFFFF8E1), SandstoneGold, "Out for Delivery / वितरण हेतु")
        "DELIVERED" -> Triple(Color(0xFFE8F5E9), OasisGreen, "Delivered / सफलतापूर्वक प्राप्त")
        "CANCELLED" -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Cancelled / रद्द")
        else -> Triple(Color(0xFFF5F5F5), Color(0xFF424242), status)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            color = textColor
        )
    }
}

@Composable
fun SellerStatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        "APPROVED" -> Triple(Color(0xFFE8F5E9), OasisGreen, "VERIFIED ARTISAN (स्वीकृत)")
        "PENDING" -> Triple(Color(0xFFFFF3E0), RoyalSaffron, "VERIFICATION PENDING (सत्यापन जारी)")
        "REJECTED" -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "REJECTED (अस्वीकृत)")
        "SUSPENDED" -> Triple(Color(0xFFECEFF1), Color(0xFF455A64), "SUSPENDED (निलंबित)")
        else -> Triple(Color(0xFFF5F5F5), Color(0xFF424242), status)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            color = textColor
        )
    }
}
