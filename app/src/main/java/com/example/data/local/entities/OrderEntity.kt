package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val district: String,
    val subtotal: Double,
    val deliveryFee: Double = 0.0,
    val totalAmount: Double,
    val paymentMethod: String, // "UPI / QR", "Cash on Delivery", "Razorpay Secured"
    val paymentStatus: String, // "PAID", "PENDING_COD"
    val orderStatus: String, // "PLACED", "PAYMENT_CONFIRMED", "PROCESSING", "PACKED", "SHIPPED", "OUT_FOR_DELIVERY", "DELIVERED", "CANCELLED"
    val trackingNumber: String,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
