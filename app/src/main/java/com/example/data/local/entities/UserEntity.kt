package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String, // "CUSTOMER", "SELLER", "ADMIN"
    val profilePhotoUrl: String = "",
    val district: String = "Jaipur",
    val isVerified: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
