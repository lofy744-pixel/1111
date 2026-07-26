package com.example.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.models.BannerItem
import com.example.models.CategoryItem
import com.example.models.OrderRequest
import com.example.models.PaymentMethod
import com.example.models.ServiceItem
import com.example.models.AppStats
import com.example.repositories.NeovaRepository
import com.example.utils.PreferencesManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NeovaRepository(application)
    private val preferencesManager = PreferencesManager(application)

    val isDarkMode: StateFlow<Boolean> = preferencesManager.isDarkMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val language: StateFlow<String> = preferencesManager.appLanguage
        .stateIn(viewModelScope, SharingStarted.Eagerly, "ar")

    val isArabic: StateFlow<Boolean> = language.map { it == "ar" }
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val services: StateFlow<List<ServiceItem>> = repository.services
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<CategoryItem>> = repository.categories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val banners: StateFlow<List<BannerItem>> = repository.banners
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val paymentMethods: StateFlow<List<PaymentMethod>> = repository.paymentMethods
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appStats: StateFlow<AppStats> = repository.appStats
        .map { it ?: repository.appStats.let { AppStats() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppStats())

    val orders: StateFlow<List<OrderRequest>> = repository.orders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchQuery = MutableStateFlow("")
    val selectedCategoryId = MutableStateFlow<String?>(null)

    val filteredServices: StateFlow<List<ServiceItem>> = combine(
        services,
        searchQuery,
        selectedCategoryId
    ) { serviceList, query, catId ->
        serviceList.filter { service ->
            val matchesCategory = catId == null || service.categoryId == catId
            val matchesQuery = query.isBlank() ||
                    service.nameAr.contains(query, ignoreCase = true) ||
                    service.nameEn.contains(query, ignoreCase = true) ||
                    service.descriptionAr.contains(query, ignoreCase = true) ||
                    service.descriptionEn.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedService = MutableStateFlow<ServiceItem?>(null)
    val selectedService: StateFlow<ServiceItem?> = _selectedService.asStateFlow()

    private val _orderSubmissionResult = MutableStateFlow<OrderRequest?>(null)
    val orderSubmissionResult: StateFlow<OrderRequest?> = _orderSubmissionResult.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    init {
        syncData()
        startPeriodicSync()
    }

    private fun startPeriodicSync() {
        viewModelScope.launch {
            while (isActive) {
                delay(15000)
                repository.syncAllData()
            }
        }
    }

    fun syncData() {
        viewModelScope.launch {
            _isSyncing.value = true
            repository.syncAllData()
            _isSyncing.value = false
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            preferencesManager.setDarkMode(!isDarkMode.value)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            preferencesManager.setLanguage(lang)
        }
    }

    fun selectService(service: ServiceItem?) {
        _selectedService.value = service
    }

    fun selectServiceById(serviceId: String) {
        viewModelScope.launch {
            _selectedService.value = repository.getServiceById(serviceId)
        }
    }

    fun clearOrderSubmissionResult() {
        _orderSubmissionResult.value = null
    }

    fun submitOrder(
        service: ServiceItem,
        customerName: String,
        phone: String,
        accountIdOrLink: String,
        quantity: Int,
        notes: String
    ) {
        viewModelScope.launch {
            val randomDigits = (10000..99999).random()
            val orderId = "NV-$randomDigits"
            val total = service.priceEgp * quantity

            val newOrder = OrderRequest(
                orderId = orderId,
                customerName = customerName.trim(),
                phone = phone.trim(),
                accountIdOrLink = accountIdOrLink.trim(),
                serviceId = service.id,
                serviceName = service.getName(isArabic.value),
                quantity = quantity,
                unitPrice = service.priceEgp,
                totalPrice = total,
                notes = notes.trim(),
                timestamp = System.currentTimeMillis(),
                status = "PENDING"
            )

            repository.createOrder(newOrder)
            _orderSubmissionResult.value = newOrder
        }
    }
}
