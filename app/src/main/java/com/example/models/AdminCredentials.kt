package com.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_credentials")
data class AdminCredentials(
    @PrimaryKey val id: String = "admin_creds",
    val username: String = "neova_store1",
    val passwordHash: String = "admin777",
    val lastUpdated: Long = System.currentTimeMillis()
)
