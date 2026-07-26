package com.example.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.models.ActivityLog
import com.example.models.AdminCredentials
import com.example.models.AppSettings
import com.example.models.BannerItem
import com.example.models.CategoryItem
import com.example.models.OrderRequest
import com.example.models.PaymentMethod
import com.example.models.ServiceItem
import com.example.models.AppStats
import kotlinx.coroutines.flow.Flow

@Dao
interface NeovaDao {
    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCategoriesCount(): Int

    @Query("SELECT COUNT(*) FROM services")
    suspend fun getServicesCount(): Int

    @Query("SELECT COUNT(*) FROM banners")
    suspend fun getBannersCount(): Int

    @Query("SELECT COUNT(*) FROM payment_methods")
    suspend fun getPaymentMethodsCount(): Int

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getOrdersCount(): Int

    @Query("SELECT * FROM services WHERE isAvailable = 1")
    fun getAllServices(): Flow<List<ServiceItem>>

    @Query("SELECT * FROM services")
    fun getAllServicesForAdmin(): Flow<List<ServiceItem>>

    @Query("SELECT * FROM services WHERE id = :serviceId")
    suspend fun getServiceById(serviceId: String): ServiceItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceItem)

    @Query("DELETE FROM services WHERE id = :serviceId")
    suspend fun deleteServiceById(serviceId: String)

    @Query("SELECT * FROM categories ORDER BY sortOrder ASC")
    fun getAllCategories(): Flow<List<CategoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryItem)

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: String)

    @Query("SELECT * FROM banners")
    fun getAllBanners(): Flow<List<BannerItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanners(banners: List<BannerItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(banner: BannerItem)

    @Query("DELETE FROM banners WHERE id = :bannerId")
    suspend fun deleteBannerById(bannerId: String)

    @Query("SELECT * FROM payment_methods")
    fun getAllPaymentMethods(): Flow<List<PaymentMethod>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentMethods(methods: List<PaymentMethod>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentMethod(method: PaymentMethod)

    @Query("DELETE FROM payment_methods WHERE id = :methodId")
    suspend fun deletePaymentMethodById(methodId: String)

    @Query("SELECT * FROM app_stats WHERE id = 'default_stats'")
    fun getAppStats(): Flow<AppStats?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppStats(stats: AppStats)

    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<OrderRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderRequest)

    @Query("UPDATE orders SET status = :status WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    @Query("DELETE FROM orders WHERE orderId = :orderId")
    suspend fun deleteOrderById(orderId: String)

    // Admin Credentials
    @Query("SELECT * FROM admin_credentials WHERE id = 'admin_creds'")
    suspend fun getAdminCredentials(): AdminCredentials?

    @Query("SELECT * FROM admin_credentials WHERE id = 'admin_creds'")
    fun getAdminCredentialsFlow(): Flow<AdminCredentials?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAdminCredentials(creds: AdminCredentials)

    // App Settings
    @Query("SELECT * FROM app_settings WHERE id = 'app_config'")
    suspend fun getAppSettings(): AppSettings?

    @Query("SELECT * FROM app_settings WHERE id = 'app_config'")
    fun getAppSettingsFlow(): Flow<AppSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAppSettings(settings: AppSettings)

    // Activity Logs
    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC")
    fun getAllActivityLogs(): Flow<List<ActivityLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLog(log: ActivityLog)

    @Query("DELETE FROM activity_logs")
    suspend fun clearAllActivityLogs()
}

