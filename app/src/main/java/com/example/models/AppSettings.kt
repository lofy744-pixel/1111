package com.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: String = "app_config",
    val appName: String = "NEOVA STORE",
    val logoUrl: String = "",
    val appIcon: String = "gamepad",
    val themeColors: String = "CYBERPUNK",
    val welcomeMessageAr: String = "أهلاً بك في NEOVA STORE - متجرك الأول لشحن الألعاب والخدمات الرقمية 🚀",
    val welcomeMessageEn: String = "Welcome to NEOVA STORE - Your #1 hub for game top-ups & digital services 🚀",
    val orderWhatsappNumber: String = "201147678818",
    val supportWhatsappNumber: String = "201147678818",
    val facebookUrl: String = "https://facebook.com",
    val telegramUrl: String = "https://t.me/neova_store",
    val instagramUrl: String = "https://instagram.com",
    val youtubeUrl: String = "https://youtube.com",
    val aboutUsAr: String = "متجر NEOVA STORE يوفر أفضل خدمات شحن الألعاب والتطبيقات والسوشيال ميديا بأسعار تنافسية وتسليم فوري.",
    val aboutUsEn: String = "NEOVA STORE provides top game top-ups, app subscriptions and social media services with fast delivery.",
    val contactUsAr: String = "يمكنك التواصل معنا على مدار 24 ساعة عبر واتساب والدعم المباشر.",
    val contactUsEn: String = "Contact us 24/7 via WhatsApp or live support.",
    val ordersEnabled: Boolean = true,
    val maintenanceMode: Boolean = false,
    val maintenanceMessageAr: String = "التطبيق قيد الصيانة المؤقتة لتحديث الخدمات. سنعود خلال دقائق! 🛠️",
    val maintenanceMessageEn: String = "App is currently under maintenance. We will be back shortly! 🛠️"
)
