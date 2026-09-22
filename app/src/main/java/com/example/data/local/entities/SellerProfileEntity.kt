package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seller_profiles")
data class SellerProfileEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val businessName: String,
    val artisanName: String,
    val category: String,
    val district: String,
    val villageOrCity: String,
    val fullAddress: String,
    val businessDescription: String,
    val artisanStory: String,
    val videoUrl: String = "",
    val bankAccountNo: String = "XXXX-XXXX-8921",
    val bankIfsc: String = "SBIN0001234",
    val status: String = "APPROVED", // "PENDING", "APPROVED", "REJECTED", "SUSPENDED"
    val isPro: Boolean = false,
    val rating: Float = 4.8f,
    val reviewCount: Int = 12,
    val createdAt: Long = System.currentTimeMillis()
)
