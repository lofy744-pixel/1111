package com.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderRequest(
    @PrimaryKey val orderId: String = "",
    val customerName: String = "",
    val phone: String = "",
    val accountIdOrLink: String = "",
    val serviceId: String = "",
    val serviceName: String = "",
    val quantity: Int = 1,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "PENDING" // PENDING, PROCESSING, COMPLETED, CANCELLED
)
