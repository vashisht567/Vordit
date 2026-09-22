package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_reviews")
data class ProductReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val userId: String,
    val userName: String,
    val userDistrict: String = "Jaipur",
    val rating: Int = 5,
    val reviewText: String,
    val isVerifiedPurchase: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
