package com.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "activity_logs")
data class ActivityLog(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val adminName: String = "neova_store1",
    val actionType: String = "INFO", // LOGIN, LOGOUT, SERVICE, CATEGORY, ORDER, PAYMENT, SETTINGS, PROFILE
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp)),
    val timeString: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
)
