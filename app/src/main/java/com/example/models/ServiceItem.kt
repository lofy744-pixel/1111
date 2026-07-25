package com.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class ServiceItem(
    @PrimaryKey val id: String = "",
    val nameAr: String = "",
    val nameEn: String = "",
    val categoryId: String = "",
    val categoryAr: String = "",
    val categoryEn: String = "",
    val descriptionAr: String = "",
    val descriptionEn: String = "",
    val priceEgp: Double = 0.0,
    val minLimit: Int = 1,
    val maxLimit: Int = 10000,
    val imageUrl: String = "",
    val isAvailable: Boolean = true
) {
    fun getName(isArabic: Boolean): String = if (isArabic) nameAr else nameEn
    fun getDescription(isArabic: Boolean): String = if (isArabic) descriptionAr else descriptionEn
    fun getCategoryName(isArabic: Boolean): String = if (isArabic) categoryAr else categoryEn
}
