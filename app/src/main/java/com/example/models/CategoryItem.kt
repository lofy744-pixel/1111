package com.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryItem(
    @PrimaryKey val id: String = "",
    val nameAr: String = "",
    val nameEn: String = "",
    val iconName: String = "gamepad",
    val sortOrder: Int = 0
) {
    fun getName(isArabic: Boolean): String = if (isArabic) nameAr else nameEn
}
