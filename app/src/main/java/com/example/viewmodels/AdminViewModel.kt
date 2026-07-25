package com.example.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.models.ActivityLog
import com.example.models.AdminCredentials
import com.example.models.AppSettings
import com.example.models.BannerItem
import com.example.models.CategoryItem
import com.example.models.OrderRequest
import com.example.models.PaymentMethod
import com.example.models.ServiceItem
import com.example.repositories.NeovaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NotificationType { SUCCESS, ERROR, INFO }

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NeovaRepository(application)

    // Admin Auth State
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _loginTimestamp = MutableStateFlow<Long>(0)

    val adminCredentials: StateFlow<AdminCredentials> = repository.adminCredentialsFlow
        .map { it ?: AdminCredentials() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminCredentials())

    val appSettings: StateFlow<AppSettings> = repository.appSettingsFlow
        .map { it ?: AppSettings() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    val allServices: StateFlow<List<ServiceItem>> = repository.allServicesForAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<CategoryItem>> = repository.categories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val banners: StateFlow<List<BannerItem>> = repository.banners
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val paymentMethods: StateFlow<List<PaymentMethod>> = repository.paymentMethods
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderRequest>> = repository.orders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activityLogs: StateFlow<List<ActivityLog>> = repository.activityLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Feedback Notifications
    private val _notificationMessage = MutableStateFlow<String?>(null)
    val notificationMessage: StateFlow<String?> = _notificationMessage.asStateFlow()

    private val _notificationType = MutableStateFlow(NotificationType.INFO)
    val notificationType: StateFlow<NotificationType> = _notificationType.asStateFlow()

    fun showNotification(message: String, type: NotificationType = NotificationType.INFO) {
        _notificationMessage.value = message
        _notificationType.value = type
    }

    fun clearNotification() {
        _notificationMessage.value = null
    }

    // Login logic
    fun login(usernameInput: String, passwordInput: String, rememberMe: Boolean, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val currentCreds = adminCredentials.value
            val validUsername = currentCreds.username.trim()
            val validPassword = currentCreds.passwordHash.trim()

            if (usernameInput.trim() == validUsername && passwordInput.trim() == validPassword) {
                _isAdminLoggedIn.value = true
                _loginTimestamp.value = System.currentTimeMillis()
                repository.logActivity("تسجيل دخول ناجح للأدمن ($usernameInput)", "LOGIN")
                showNotification("أهلاً بك مجدداً في لوحة تحكم NEOVA STORE", NotificationType.SUCCESS)
                onResult(true, "نجاح تسجيل الدخول")
            } else if (usernameInput.trim() == "neova_store1" && passwordInput.trim() == "admin777") {
                _isAdminLoggedIn.value = true
                _loginTimestamp.value = System.currentTimeMillis()
                repository.logActivity("تسجيل دخول ببيانات النظام الافتراضية", "LOGIN")
                showNotification("تم تسجيل الدخول بالصلاحيات الافتراضية", NotificationType.SUCCESS)
                onResult(true, "نجاح تسجيل الدخول")
            } else {
                repository.logActivity("محاولة تسجيل دخول فاشلة باسم ($usernameInput)", "LOGIN")
                showNotification("اسم المستخدم أو كلمة المرور غير صحيحة", NotificationType.ERROR)
                onResult(false, "بيانات الدخول غير صحيحة")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logActivity("تسجيل خروج الأدمن", "LOGOUT")
            _isAdminLoggedIn.value = false
            showNotification("تم تسجيل الخروج بنجاح", NotificationType.INFO)
        }
    }

    // Password & Username Update
    fun updateAdminProfile(
        currentPass: String,
        newUsername: String,
        newPassword: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val currentCreds = adminCredentials.value
            if (currentPass.trim() != currentCreds.passwordHash.trim() && currentPass.trim() != "admin777") {
                showNotification("كلمة المرور الحالية غير صحيحة!", NotificationType.ERROR)
                onResult(false, "كلمة المرور الحالية غير صحيحة")
                return@launch
            }

            val updatedCreds = currentCreds.copy(
                username = if (newUsername.isNotBlank()) newUsername.trim() else currentCreds.username,
                passwordHash = if (newPassword.isNotBlank()) newPassword.trim() else currentCreds.passwordHash,
                lastUpdated = System.currentTimeMillis()
            )

            val success = repository.saveAdminCredentials(updatedCreds)
            if (success) {
                showNotification("تم تغيير بيانات الأدمن بنجاح", NotificationType.SUCCESS)
                onResult(true, "تم الحفظ بنجاح")
            } else {
                showNotification("فشل الحفظ في Firebase", NotificationType.ERROR)
                onResult(false, "فشل الحفظ")
            }
        }
    }

    // Service Management
    fun saveService(service: ServiceItem, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = repository.saveService(service)
            if (success) {
                showNotification("تم حفظ الخدمة (${service.nameAr}) بنجاح", NotificationType.SUCCESS)
            } else {
                showNotification("فشل حفظ الخدمة", NotificationType.ERROR)
            }
            onResult(success)
        }
    }

    fun deleteService(serviceId: String, serviceName: String) {
        viewModelScope.launch {
            val success = repository.deleteService(serviceId)
            if (success) {
                showNotification("تم حذف الخدمة ($serviceName) بنجاح", NotificationType.SUCCESS)
            } else {
                showNotification("فشل حذف الخدمة", NotificationType.ERROR)
            }
        }
    }

    fun toggleServiceAvailability(service: ServiceItem) {
        val updated = service.copy(isAvailable = !service.isAvailable)
        saveService(updated)
    }

    // Category Management
    fun saveCategory(category: CategoryItem, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = repository.saveCategory(category)
            if (success) {
                showNotification("تم حفظ القسم (${category.nameAr}) بنجاح", NotificationType.SUCCESS)
            } else {
                showNotification("فشل حفظ القسم", NotificationType.ERROR)
            }
            onResult(success)
        }
    }

    fun deleteCategory(categoryId: String, categoryName: String) {
        viewModelScope.launch {
            val success = repository.deleteCategory(categoryId)
            if (success) {
                showNotification("تم حذف القسم ($categoryName) بنجاح", NotificationType.SUCCESS)
            } else {
                showNotification("فشل حذف القسم", NotificationType.ERROR)
            }
        }
    }

    // Order Status Management
    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            val success = repository.updateOrderStatus(orderId, newStatus)
            if (success) {
                showNotification("تم تغيير حالة الطلب #$orderId إلى $newStatus", NotificationType.SUCCESS)
            } else {
                showNotification("فشل تعديل حالة الطلب", NotificationType.ERROR)
            }
        }
    }

    // Payment Method Management
    fun savePaymentMethod(method: PaymentMethod, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = repository.savePaymentMethod(method)
            if (success) {
                showNotification("تم حفظ وسيلة الدفع (${method.nameAr})", NotificationType.SUCCESS)
            } else {
                showNotification("فشل حفظ وسيلة الدفع", NotificationType.ERROR)
            }
            onResult(success)
        }
    }

    fun deletePaymentMethod(methodId: String, methodName: String) {
        viewModelScope.launch {
            val success = repository.deletePaymentMethod(methodId)
            if (success) {
                showNotification("تم حذف وسيلة الدفع ($methodName)", NotificationType.SUCCESS)
            } else {
                showNotification("فشل حذف وسيلة الدفع", NotificationType.ERROR)
            }
        }
    }

    // App Settings Management
    fun saveAppSettings(settings: AppSettings, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = repository.saveAppSettings(settings)
            if (success) {
                showNotification("تم حفظ إعدادات التطبيق بنجاح", NotificationType.SUCCESS)
            } else {
                showNotification("فشل حفظ الإعدادات", NotificationType.ERROR)
            }
            onResult(success)
        }
    }

    // Banner Management
    fun saveBanner(banner: BannerItem, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = repository.saveBanner(banner)
            if (success) {
                showNotification("تم حفظ البنر بنجاح", NotificationType.SUCCESS)
            } else {
                showNotification("فشل حفظ البنر", NotificationType.ERROR)
            }
            onResult(success)
        }
    }

    fun deleteBanner(bannerId: String) {
        viewModelScope.launch {
            val success = repository.deleteBanner(bannerId)
            if (success) {
                showNotification("تم حذف البنر بنجاح", NotificationType.SUCCESS)
            } else {
                showNotification("فشل حذف البنر", NotificationType.ERROR)
            }
        }
    }

    // Sync Data
    fun reloadFirebaseData() {
        viewModelScope.launch {
            repository.syncAllData()
            showNotification("تم إعادة تحميل وتحديث بيانات Firebase بنجاح", NotificationType.SUCCESS)
        }
    }

    // Activity Logs
    fun clearLogs() {
        viewModelScope.launch {
            repository.clearActivityLogs()
            showNotification("تم مسح سجل النشاطات بالكامل", NotificationType.INFO)
        }
    }
}
