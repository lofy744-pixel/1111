package com.example.repositories

import android.content.Context
import com.example.db.NeovaDao
import com.example.db.NeovaDatabase
import com.example.firebase.FirebaseService
import com.example.models.ActivityLog
import com.example.models.AdminCredentials
import com.example.models.AppSettings
import com.example.models.BannerItem
import com.example.models.CategoryItem
import com.example.models.OrderRequest
import com.example.models.PaymentMethod
import com.example.models.ServiceItem
import com.example.models.AppStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NeovaRepository(context: Context) {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val dao: NeovaDao = NeovaDatabase.getDatabase(context).neovaDao()
    private val firebaseService: FirebaseService = FirebaseService(context)

    val services: Flow<List<ServiceItem>> = dao.getAllServices()
    val allServicesForAdmin: Flow<List<ServiceItem>> = dao.getAllServicesForAdmin()
    val categories: Flow<List<CategoryItem>> = dao.getAllCategories()
    val banners: Flow<List<BannerItem>> = dao.getAllBanners()
    val paymentMethods: Flow<List<PaymentMethod>> = dao.getAllPaymentMethods()
    val appStats: Flow<AppStats?> = dao.getAppStats()
    val orders: Flow<List<OrderRequest>> = dao.getAllOrders()
    val activityLogs: Flow<List<ActivityLog>> = dao.getAllActivityLogs()
    val appSettingsFlow: Flow<AppSettings?> = dao.getAppSettingsFlow()
    val adminCredentialsFlow: Flow<AdminCredentials?> = dao.getAdminCredentialsFlow()

    init {
        startRealtimeListeners()
    }

    private fun startRealtimeListeners() {
        firebaseService.listenToOrders { orderList ->
            repositoryScope.launch {
                orderList.forEach { dao.insertOrder(it) }
            }
        }

        firebaseService.listenToServices { serviceList ->
            repositoryScope.launch {
                dao.replaceAllServices(serviceList)
            }
        }

        firebaseService.listenToCategories { categoryList ->
            repositoryScope.launch {
                dao.replaceAllCategories(categoryList)
            }
        }

        firebaseService.listenToBanners { bannerList ->
            repositoryScope.launch {
                dao.replaceAllBanners(bannerList)
            }
        }

        firebaseService.listenToPaymentMethods { paymentList ->
            repositoryScope.launch {
                dao.replaceAllPaymentMethods(paymentList)
            }
        }

        firebaseService.listenToAppSettings { settings ->
            repositoryScope.launch {
                dao.saveAppSettings(settings)
            }
        }
    }

    suspend fun syncAllData() = withContext(Dispatchers.IO) {
        try {
            // Seed local Room DB immediately with defaults ONLY if empty
            seedDefaultsIfNecessary()

            // Fetch live data from Firestore and sync into Room
            val remoteCategories = firebaseService.getCategoriesFromFirestore()
            if (remoteCategories.isNotEmpty()) {
                dao.insertCategories(remoteCategories)
            }

            val remoteServices = firebaseService.getServicesFromFirestore()
            if (remoteServices.isNotEmpty()) {
                dao.insertServices(remoteServices)
            }

            val remoteBanners = firebaseService.getBannersFromFirestore()
            if (remoteBanners.isNotEmpty()) {
                dao.insertBanners(remoteBanners)
            }

            val remotePaymentMethods = firebaseService.getPaymentMethodsFromFirestore()
            if (remotePaymentMethods.isNotEmpty()) {
                dao.insertPaymentMethods(remotePaymentMethods)
            }

            val remoteOrders = firebaseService.getOrdersFromFirestore()
            if (remoteOrders.isNotEmpty()) {
                remoteOrders.forEach { dao.insertOrder(it) }
            }

            val remoteStats = firebaseService.getAppStatsFromFirestore()
            dao.insertAppStats(remoteStats)

            val remoteCreds = firebaseService.getAdminCredentialsFromFirestore()
            if (remoteCreds != null) {
                dao.saveAdminCredentials(remoteCreds)
            } else if (dao.getAdminCredentials() == null) {
                val defaultCreds = AdminCredentials()
                dao.saveAdminCredentials(defaultCreds)
                firebaseService.saveAdminCredentialsToFirestore(defaultCreds)
            }

            val remoteSettings = firebaseService.getAppSettingsFromFirestore()
            if (remoteSettings != null) {
                dao.saveAppSettings(remoteSettings)
            } else if (dao.getAppSettings() == null) {
                val defaultSettings = AppSettings()
                dao.saveAppSettings(defaultSettings)
                firebaseService.saveAppSettingsToFirestore(defaultSettings)
            }

            val remoteLogs = firebaseService.getActivityLogsFromFirestore()
            if (remoteLogs.isNotEmpty()) {
                remoteLogs.forEach { dao.insertActivityLog(it) }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            seedDefaultsIfNecessary()
        }
    }

    private suspend fun seedDefaultsIfNecessary() {
        if (dao.getCategoriesCount() == 0) {
            val categories = firebaseService.getDefaultCategories()
            dao.insertCategories(categories)
            categories.forEach { firebaseService.saveCategoryToFirestore(it) }
        }
        if (dao.getServicesCount() == 0) {
            val services = firebaseService.getDefaultServices()
            dao.insertServices(services)
            services.forEach { firebaseService.saveServiceToFirestore(it) }
        }
        if (dao.getBannersCount() == 0) {
            val banners = firebaseService.getDefaultBanners()
            dao.insertBanners(banners)
            banners.forEach { firebaseService.saveBannerToFirestore(it) }
        }
        if (dao.getPaymentMethodsCount() == 0) {
            val paymentMethods = firebaseService.getDefaultPaymentMethods()
            dao.insertPaymentMethods(paymentMethods)
            paymentMethods.forEach { firebaseService.savePaymentMethodToFirestore(it) }
        }
        if (dao.getAdminCredentials() == null) {
            val creds = AdminCredentials()
            dao.saveAdminCredentials(creds)
            firebaseService.saveAdminCredentialsToFirestore(creds)
        }
        if (dao.getAppSettings() == null) {
            val settings = AppSettings()
            dao.saveAppSettings(settings)
            firebaseService.saveAppSettingsToFirestore(settings)
        }
        if (dao.getOrdersCount() == 0) {
            val testOrder = OrderRequest(
                orderId = "NV-88291",
                customerName = "عميل تجريبي - NEOVA STORE",
                phone = "01012345678",
                accountIdOrLink = "https://instagram.com/neovastore_test",
                serviceId = "srv_1",
                serviceName = "متابعين إنستجرام حقيقيين 🚀",
                quantity = 1000,
                unitPrice = 150.0,
                totalPrice = 150.0,
                notes = "طلب تجريبي تلقائي تم إنشاؤه للتحقق من ظهور مجموعة orders في Cloud Firestore لوحة الأدمن",
                timestamp = System.currentTimeMillis(),
                status = "PENDING"
            )
            val savedRemote = firebaseService.saveOrderToFirestore(testOrder)
            if (savedRemote) {
                dao.insertOrder(testOrder)
                android.util.Log.d("NeovaRepository", "Successfully created test order NV-88291 directly in Firestore collection 'orders'")
            } else {
                android.util.Log.e("NeovaRepository", "Failed to create test order in Firestore collection 'orders'")
            }
        }
    }

    suspend fun createOrder(order: OrderRequest): Boolean = withContext(Dispatchers.IO) {
        val savedRemote = firebaseService.saveOrderToFirestore(order)
        if (savedRemote) {
            dao.insertOrder(order)
            logActivity("طلب جديد #${order.orderId}", "ORDER")
            true
        } else {
            android.util.Log.e("NeovaRepository", "Failed to write order #${order.orderId} to Firestore 'orders' collection")
            false
        }
    }

    suspend fun updateOrderStatus(orderId: String, status: String): Boolean = withContext(Dispatchers.IO) {
        val savedRemote = firebaseService.updateOrderStatusInFirestore(orderId, status)
        if (savedRemote) {
            dao.updateOrderStatus(orderId, status)
            logActivity("تعديل حالة الطلب #$orderId إلى $status", "ORDER")
            true
        } else {
            android.util.Log.e("NeovaRepository", "Failed to update status of order #$orderId in Firestore 'orders'")
            false
        }
    }

    suspend fun getServiceById(serviceId: String): ServiceItem? = withContext(Dispatchers.IO) {
        dao.getServiceById(serviceId)
    }

    // Service CRUD
    suspend fun saveService(service: ServiceItem): Boolean = withContext(Dispatchers.IO) {
        dao.insertService(service)
        firebaseService.saveServiceToFirestore(service)
        logActivity("حفظ الخدمة ${service.nameAr}", "SERVICE")
        true
    }

    suspend fun deleteService(serviceId: String): Boolean = withContext(Dispatchers.IO) {
        dao.deleteServiceById(serviceId)
        firebaseService.deleteServiceFromFirestore(serviceId)
        logActivity("حذف الخدمة #$serviceId", "SERVICE")
        true
    }

    // Category CRUD
    suspend fun saveCategory(category: CategoryItem): Boolean = withContext(Dispatchers.IO) {
        dao.insertCategory(category)
        firebaseService.saveCategoryToFirestore(category)
        logActivity("حفظ القسم ${category.nameAr}", "CATEGORY")
        true
    }

    suspend fun deleteCategory(categoryId: String): Boolean = withContext(Dispatchers.IO) {
        dao.deleteCategoryById(categoryId)
        firebaseService.deleteCategoryFromFirestore(categoryId)
        logActivity("حذف القسم #$categoryId", "CATEGORY")
        true
    }

    // Payment Method CRUD
    suspend fun savePaymentMethod(method: PaymentMethod): Boolean = withContext(Dispatchers.IO) {
        dao.insertPaymentMethod(method)
        firebaseService.savePaymentMethodToFirestore(method)
        logActivity("حفظ وسيلة الدفع ${method.nameAr}", "PAYMENT")
        true
    }

    suspend fun deletePaymentMethod(methodId: String): Boolean = withContext(Dispatchers.IO) {
        dao.deletePaymentMethodById(methodId)
        firebaseService.deletePaymentMethodFromFirestore(methodId)
        logActivity("حذف وسيلة الدفع #$methodId", "PAYMENT")
        true
    }

    // Banner CRUD
    suspend fun saveBanner(banner: BannerItem): Boolean = withContext(Dispatchers.IO) {
        dao.insertBanner(banner)
        firebaseService.saveBannerToFirestore(banner)
        logActivity("حفظ البنر ${banner.titleAr}", "SETTINGS")
        true
    }

    suspend fun deleteBanner(bannerId: String): Boolean = withContext(Dispatchers.IO) {
        dao.deleteBannerById(bannerId)
        firebaseService.deleteBannerFromFirestore(bannerId)
        logActivity("حذف البنر #$bannerId", "SETTINGS")
        true
    }

    // Admin Credentials
    suspend fun getAdminCredentials(): AdminCredentials {
        return withContext(Dispatchers.IO) {
            dao.getAdminCredentials() ?: AdminCredentials()
        }
    }

    suspend fun saveAdminCredentials(creds: AdminCredentials): Boolean = withContext(Dispatchers.IO) {
        dao.saveAdminCredentials(creds)
        firebaseService.saveAdminCredentialsToFirestore(creds)
        logActivity("تغيير بيانات الأدمن", "PROFILE")
        true
    }

    // App Settings
    suspend fun getAppSettings(): AppSettings {
        return withContext(Dispatchers.IO) {
            dao.getAppSettings() ?: AppSettings()
        }
    }

    suspend fun saveAppSettings(settings: AppSettings): Boolean = withContext(Dispatchers.IO) {
        dao.saveAppSettings(settings)
        firebaseService.saveAppSettingsToFirestore(settings)
        logActivity("تحديث إعدادات التطبيق", "SETTINGS")
        true
    }

    // Activity Logs
    suspend fun logActivity(title: String, type: String = "INFO", adminName: String = "neova_store1") = withContext(Dispatchers.IO) {
        val log = ActivityLog(
            id = "log_${System.currentTimeMillis()}_${(100..999).random()}",
            title = title,
            adminName = adminName,
            actionType = type,
            timestamp = System.currentTimeMillis()
        )
        dao.insertActivityLog(log)
        firebaseService.saveActivityLogToFirestore(log)
    }

    suspend fun clearActivityLogs() = withContext(Dispatchers.IO) {
        dao.clearAllActivityLogs()
    }
}

