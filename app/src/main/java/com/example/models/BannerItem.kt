package com.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banners")
data class BannerItem(
    @PrimaryKey val id: String = "",
    val titleAr: String = "",
    val titleEn: String = "",
    val subtitleAr: String = "",
    val subtitleEn: String = "",
    val imageUrl: String = "",
    val targetCategoryId: String = ""
) {
    fun getTitle(isArabic: Boolean): String = if (isArabic) titleAr else titleEn
    fun getSubtitle(isArabic: Boolean): String = if (isArabic) subtitleAr else subtitleEn
}
