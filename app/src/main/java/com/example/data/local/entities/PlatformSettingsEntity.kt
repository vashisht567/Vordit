package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "platform_settings")
data class PlatformSettingsEntity(
    @PrimaryKey val id: String = "global",
    val commissionPercentage: Double = 8.0,
    val districtsJson: String = "Jaipur,Jodhpur,Udaipur,Bikaner,Kota,Ajmer,Jaisalmer,Banswara,Dungarpur,Barmer,Chittorgarh,Bhilwara,Pali,Nagaur,Sirohi,Alwar,Sikar,Pratapgarh,Sawai Madhopur,Tonk",
    val isDemoDataActive: Boolean = true,
    val supportPhone: String = "+91 98290 12345",
    val supportEmail: String = "support@padharo.rajasthan.in"
)
