package com.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_methods")
data class PaymentMethod(
    @PrimaryKey val id: String = "",
    val nameAr: String = "",
    val nameEn: String = "",
    val number: String = "",
    val iconType: String = "wallet", // vodafone, etisalat, instapay
    val instructionsAr: String = "",
    val instructionsEn: String = ""
) {
    fun getName(isArabic: Boolean): String = if (isArabic) nameAr else nameEn
    fun getInstructions(isArabic: Boolean): String = if (isArabic) instructionsAr else instructionsEn
}
