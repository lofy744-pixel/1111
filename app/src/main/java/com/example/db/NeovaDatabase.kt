package com.example.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.models.ActivityLog
import com.example.models.AdminCredentials
import com.example.models.AppSettings
import com.example.models.BannerItem
import com.example.models.CategoryItem
import com.example.models.OrderRequest
import com.example.models.PaymentMethod
import com.example.models.ServiceItem
import com.example.models.AppStats

@Database(
    entities = [
        ServiceItem::class,
        CategoryItem::class,
        BannerItem::class,
        PaymentMethod::class,
        OrderRequest::class,
        AppStats::class,
        AdminCredentials::class,
        ActivityLog::class,
        AppSettings::class
    ],
    version = 2,
    exportSchema = false
)
abstract class NeovaDatabase : RoomDatabase() {
    abstract fun neovaDao(): NeovaDao

    companion object {
        @Volatile
        private var INSTANCE: NeovaDatabase? = null

        fun getDatabase(context: Context): NeovaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NeovaDatabase::class.java,
                    "neova_store_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
