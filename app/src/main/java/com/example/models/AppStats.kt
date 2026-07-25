package com.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_stats")
data class AppStats(
    @PrimaryKey val id: String = "default_stats",
    val baseClientsCount: Int = 1211,
    val baseTimestamp: Long = 1700000000000L, // Fixed epoch reference
    val manualClientsDelta: Int = 0,
    val ordersCount: Int = 3480,
    val servicesCount: Int = 42,
    val visitsCount: Int = 18950,
    val welcomeMessageAr: String = "أهلاً بك في NEOVA STORE - متجرك الأول لشحن الألعاب والخدمات الرقمية 🚀",
    val welcomeMessageEn: String = "Welcome to NEOVA STORE - Your #1 hub for game top-ups & digital services 🚀"
) {
    // Calculates current clients count starting from 1211, adding +1 every 2 days (172800000 ms)
    fun getDynamicClientsCount(): Int {
        val now = System.currentTimeMillis()
        val daysElapsed = if (now > baseTimestamp) (now - baseTimestamp) / (1000 * 60 * 60 * 24) else 0
        val autoIncrement = (daysElapsed / 2).toInt()
        return baseClientsCount + autoIncrement + manualClientsDelta
    }

    fun getWelcomeMessage(isArabic: Boolean): String =
        if (isArabic) welcomeMessageAr else welcomeMessageEn
}
