package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val sellerId: String,
    val name: String,
    val hindiName: String = "",
    val category: String,
    val description: String,
    val hindiDescription: String = "",
    val shortDescription: String = "",
    val price: Double,
    val discountPercent: Int = 0,
    val stock: Int = 10,
    val sku: String = "",
    val weightGrams: Int = 500,
    val dimensions: String = "20x15x10 cm",
    val imageUrl: String,
    val district: String,
    val makerStory: String = "",
    val makerName: String = "",
    val tags: String = "", // Comma-separated
    val isApproved: Boolean = true,
    val isFeatured: Boolean = false,
    val rating: Float = 4.8f,
    val reviewCount: Int = 0,
    val isDemo: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
