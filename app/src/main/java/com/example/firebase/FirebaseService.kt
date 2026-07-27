package com.example.firebase

import android.content.Context
import android.util.Log
import com.example.models.ActivityLog
import com.example.models.AdminCredentials
import com.example.models.AppSettings
import com.example.models.BannerItem
import com.example.models.CategoryItem
import com.example.models.OrderRequest
import com.example.models.PaymentMethod
import com.example.models.ServiceItem
import com.example.models.AppStats
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class FirebaseService(private val context: Context? = null) {

    companion object {
        @Volatile
        private var isInitialized = false
    }

    private fun getDb(): FirebaseFirestore? {
        return try {
            val ctx = context
            if (ctx != null && !isInitialized) {
                if (FirebaseApp.getApps(ctx).isEmpty()) {
                    FirebaseApp.initializeApp(ctx)
                }
                isInitialized = true
            }
            FirebaseFirestore.getInstance()
        } catch (t: Throwable) {
            Log.e("FirebaseService", "Firestore Error (getDb): ${t.localizedMessage ?: t.message}", t)
            null
        }
    }

    suspend fun getServicesFromFirestore(): List<ServiceItem> {
        val database = getDb() ?: return getDefaultServices()
        return try {
            val snapshot = database.collection("services").get().await()
            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ServiceItem::class.java)
            }
            if (list.isEmpty()) getDefaultServices() else list
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not fetch services from Firestore: ${e.message}")
            getDefaultServices()
        }
    }

    suspend fun getCategoriesFromFirestore(): List<CategoryItem> {
        val database = getDb() ?: return getDefaultCategories()
        return try {
            val snapshot = database.collection("categories").get().await()
            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(CategoryItem::class.java)
            }
            if (list.isEmpty()) getDefaultCategories() else list
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not fetch categories from Firestore: ${e.message}")
            getDefaultCategories()
        }
    }

    suspend fun getBannersFromFirestore(): List<BannerItem> {
        val database = getDb() ?: return getDefaultBanners()
        return try {
            val snapshot = database.collection("banners").get().await()
            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(BannerItem::class.java)
            }
            if (list.isEmpty()) getDefaultBanners() else list
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not fetch banners from Firestore: ${e.message}")
            getDefaultBanners()
        }
    }

    suspend fun getPaymentMethodsFromFirestore(): List<PaymentMethod> {
        val database = getDb() ?: return getDefaultPaymentMethods()
        return try {
            val snapshot = database.collection("payment_methods").get().await()
            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(PaymentMethod::class.java)
            }
            if (list.isEmpty()) getDefaultPaymentMethods() else list
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not fetch payment methods from Firestore: ${e.message}")
            getDefaultPaymentMethods()
        }
    }

    suspend fun getAppStatsFromFirestore(): AppStats {
        val database = getDb() ?: return getDefaultStats()
        return try {
            val doc = database.collection("app_stats").document("default_stats").get().await()
            doc.toObject(AppStats::class.java) ?: getDefaultStats()
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not fetch app stats from Firestore: ${e.message}")
            getDefaultStats()
        }
    }

    suspend fun saveOrderToFirestore(order: OrderRequest): Pair<Boolean, String?> {
        val database = getDb()
        if (database == null) {
            val err = "Firestore instance is null. Please verify Firebase setup."
            Log.e("FirebaseService", "CRITICAL ERROR: $err")
            return Pair(false, err)
        }
        return try {
            Log.d("FirebaseService", "Writing order #${order.orderId} to Firestore collection 'orders'...")
            database.collection("orders").document(order.orderId).set(order).await()
            Log.d("FirebaseService", "SUCCESSFULLY saved order #${order.orderId} to Firestore collection 'orders'")
            Pair(true, null)
        } catch (e: Exception) {
            val errorMsg = e.localizedMessage ?: e.message ?: e.toString()
            Log.e("FirebaseService", "Firestore Error (orders.setDoc): $errorMsg", e)
            Pair(false, errorMsg)
        }
    }

    suspend fun getOrdersFromFirestore(): List<OrderRequest> {
        val database = getDb() ?: return emptyList()
        return try {
            val snapshot = database.collection("orders").get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(OrderRequest::class.java)
                } catch (e: Exception) {
                    Log.e("FirebaseService", "Error deserializing OrderRequest from doc ${doc.id}: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseService", "Could not fetch orders from Firestore collection 'orders': ${e.message}", e)
            emptyList()
        }
    }

    suspend fun updateOrderStatusInFirestore(orderId: String, newStatus: String): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("orders").document(orderId).update("status", newStatus).await()
            Log.d("FirebaseService", "Successfully updated order #$orderId status to '$newStatus' in Firestore 'orders'")
            true
        } catch (e: Exception) {
            Log.e("FirebaseService", "Could not update order status in Firestore: ${e.message}", e)
            false
        }
    }

    suspend fun saveServiceToFirestore(service: ServiceItem): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("services").document(service.id).set(service).await()
            true
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not save service to Firestore: ${e.message}")
            false
        }
    }

    suspend fun deleteServiceFromFirestore(serviceId: String): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("services").document(serviceId).delete().await()
            true
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not delete service from Firestore: ${e.message}")
            false
        }
    }

    suspend fun saveCategoryToFirestore(category: CategoryItem): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("categories").document(category.id).set(category).await()
            true
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not save category to Firestore: ${e.message}")
            false
        }
    }

    suspend fun deleteCategoryFromFirestore(categoryId: String): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("categories").document(categoryId).delete().await()
            true
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not delete category from Firestore: ${e.message}")
            false
        }
    }

    suspend fun savePaymentMethodToFirestore(method: PaymentMethod): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("payment_methods").document(method.id).set(method).await()
            true
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not save payment method to Firestore: ${e.message}")
            false
        }
    }

    suspend fun deletePaymentMethodFromFirestore(methodId: String): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("payment_methods").document(methodId).delete().await()
            true
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not delete payment method from Firestore: ${e.message}")
            false
        }
    }

    suspend fun saveBannerToFirestore(banner: BannerItem): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("banners").document(banner.id).set(banner).await()
            true
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not save banner to Firestore: ${e.message}")
            false
        }
    }

    suspend fun deleteBannerFromFirestore(bannerId: String): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("banners").document(bannerId).delete().await()
            true
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not delete banner from Firestore: ${e.message}")
            false
        }
    }

    suspend fun getAdminCredentialsFromFirestore(): AdminCredentials? {
        val database = getDb() ?: return null
        return try {
            val doc = database.collection("admin_config").document("credentials").get().await()
            doc.toObject(AdminCredentials::class.java)
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not fetch admin credentials from Firestore: ${e.message}")
            null
        }
    }

    suspend fun saveAdminCredentialsToFirestore(creds: AdminCredentials): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("admin_config").document("credentials").set(creds).await()
            true
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not save admin credentials to Firestore: ${e.message}")
            false
        }
    }

    suspend fun getAppSettingsFromFirestore(): AppSettings? {
        val database = getDb() ?: return null
        return try {
            val doc = database.collection("app_config").document("settings").get().await()
            doc.toObject(AppSettings::class.java)
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not fetch app settings from Firestore: ${e.message}")
            null
        }
    }

    suspend fun saveAppSettingsToFirestore(settings: AppSettings): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("app_config").document("settings").set(settings).await()
            true
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not save app settings to Firestore: ${e.message}")
            false
        }
    }

    suspend fun getActivityLogsFromFirestore(): List<ActivityLog> {
        val database = getDb() ?: return emptyList()
        return try {
            val snapshot = database.collection("activity_logs").get().await()
            snapshot.documents.mapNotNull { it.toObject(ActivityLog::class.java) }
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not fetch activity logs from Firestore: ${e.message}")
            emptyList()
        }
    }

    suspend fun saveActivityLogToFirestore(log: ActivityLog): Boolean {
        val database = getDb() ?: return false
        return try {
            database.collection("activity_logs").document(log.id).set(log).await()
            true
        } catch (e: Exception) {
            Log.w("FirebaseService", "Could not save activity log to Firestore: ${e.message}")
            false
        }
    }

    // Real-time Firestore Snapshot Listeners for Continuous Auto-Update
    fun listenToOrders(onUpdate: (List<OrderRequest>) -> Unit): ListenerRegistration? {
        val database = getDb()
        if (database == null) {
            Log.e("FirebaseService", "CRITICAL ERROR: Firestore instance is null in listenToOrders")
            return null
        }
        return try {
            Log.d("FirebaseService", "Registering addSnapshotListener on Firestore collection 'orders'...")
            database.collection("orders").addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirebaseService", "Firestore 'orders' collection SnapshotListener error: ${error.message}", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val orders = snapshot.documents.mapNotNull { doc ->
                        try {
                            doc.toObject(OrderRequest::class.java)
                        } catch (e: Exception) {
                            Log.e("FirebaseService", "Error parsing OrderRequest doc ${doc.id}: ${e.message}")
                            null
                        }
                    }
                    Log.d("FirebaseService", "SnapshotListener received ${orders.size} orders from Firestore 'orders'")
                    onUpdate(orders)
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseService", "Failed to register orders listener: ${e.message}", e)
            null
        }
    }

    fun listenToServices(onUpdate: (List<ServiceItem>) -> Unit): ListenerRegistration? {
        val database = getDb() ?: return null
        return try {
            database.collection("services").addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(ServiceItem::class.java) }
                    if (list.isNotEmpty()) onUpdate(list)
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun listenToCategories(onUpdate: (List<CategoryItem>) -> Unit): ListenerRegistration? {
        val database = getDb() ?: return null
        return try {
            database.collection("categories").addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(CategoryItem::class.java) }
                    if (list.isNotEmpty()) onUpdate(list)
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun listenToBanners(onUpdate: (List<BannerItem>) -> Unit): ListenerRegistration? {
        val database = getDb() ?: return null
        return try {
            database.collection("banners").addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(BannerItem::class.java) }
                    if (list.isNotEmpty()) onUpdate(list)
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun listenToPaymentMethods(onUpdate: (List<PaymentMethod>) -> Unit): ListenerRegistration? {
        val database = getDb() ?: return null
        return try {
            database.collection("payment_methods").addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(PaymentMethod::class.java) }
                    if (list.isNotEmpty()) onUpdate(list)
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun listenToAppSettings(onUpdate: (AppSettings) -> Unit): ListenerRegistration? {
        val database = getDb() ?: return null
        return try {
            database.collection("app_config").document("settings").addSnapshotListener { doc, error ->
                if (error != null) return@addSnapshotListener
                if (doc != null && doc.exists()) {
                    doc.toObject(AppSettings::class.java)?.let { onUpdate(it) }
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    // Default Seed Data Generator for local fallback & initial Firestore sync
    fun getDefaultCategories(): List<CategoryItem> = listOf(
        CategoryItem("cat_games", "شحن الألعاب", "Game Top-up", "gamepad", 1),
        CategoryItem("cat_apps", "شحن التطبيقات", "App Subscriptions", "apps", 2),
        CategoryItem("cat_social", "السوشيال ميديا", "Social Media", "share", 3),
        CategoryItem("cat_other", "خدمات أخرى", "Other Services", "widgets", 4)
    )

    fun getDefaultBanners(): List<BannerItem> = listOf(
        BannerItem(
            id = "banner_1",
            titleAr = "عروض الشحن الفوري للألعاب 🎮",
            titleEn = "Instant Game Recharge Offers 🎮",
            subtitleAr = "أفضل الأسعار وشحن تلقائي خلال دقائق",
            subtitleEn = "Best prices & auto delivery within minutes",
            imageUrl = "img_banner_gaming",
            targetCategoryId = "cat_games"
        ),
        BannerItem(
            id = "banner_2",
            titleAr = "شحن اشتراكات التطبيقات ⚡",
            titleEn = "Top-up App Subscriptions ⚡",
            subtitleAr = "شاهد، نيتفليكس، وباقات الإنترنت بخصومات حصريّة",
            subtitleEn = "Shahid, Netflix & data packages with discounts",
            imageUrl = "img_banner_services",
            targetCategoryId = "cat_apps"
        )
    )

    fun getDefaultServices(): List<ServiceItem> = listOf(
        // Game Recharge Services
        ServiceItem(
            id = "srv_pubg_60",
            nameAr = "شحن ببجي مبايل (60 شدة)",
            nameEn = "PUBG Mobile (60 UC)",
            categoryId = "cat_games",
            categoryAr = "شحن الألعاب",
            categoryEn = "Game Top-up",
            descriptionAr = "شحن فوري ومباشر بحساب ID لـ 60 شدة ببجي موبايل الرسمية.",
            descriptionEn = "Instant direct top-up to Player ID for 60 Official PUBG Mobile UC.",
            priceEgp = 55.0,
            minLimit = 1,
            maxLimit = 50,
            imageUrl = "",
            isAvailable = true
        ),
        ServiceItem(
            id = "srv_pubg_325",
            nameAr = "شحن ببجي مبايل (325 شدة)",
            nameEn = "PUBG Mobile (325 UC)",
            categoryId = "cat_games",
            categoryAr = "شحن الألعاب",
            categoryEn = "Game Top-up",
            descriptionAr = "شحن مباشر بحساب ID لـ 325 شدة ببجي مبايل مع تسليم فوري.",
            descriptionEn = "Direct Player ID top-up for 325 PUBG Mobile UC with instant delivery.",
            priceEgp = 280.0,
            minLimit = 1,
            maxLimit = 20,
            imageUrl = "",
            isAvailable = true
        ),
        ServiceItem(
            id = "srv_freefire_110",
            nameAr = "فري فاير (110 جوهرة)",
            nameEn = "Free Fire (110 Diamonds)",
            categoryId = "cat_games",
            categoryAr = "شحن الألعاب",
            categoryEn = "Game Top-up",
            descriptionAr = "شحن فوري بالمعرف ID لـ 110 جوهرة فري فاير.",
            descriptionEn = "Instant ID top-up for 110 Free Fire Diamonds.",
            priceEgp = 45.0,
            minLimit = 1,
            maxLimit = 100,
            imageUrl = "",
            isAvailable = true
        ),
        ServiceItem(
            id = "srv_freefire_580",
            nameAr = "فري فاير (580 جوهرة)",
            nameEn = "Free Fire (580 Diamonds)",
            categoryId = "cat_games",
            categoryAr = "شحن الألعاب",
            categoryEn = "Game Top-up",
            descriptionAr = "باقت الجواهر الكبيرة لفري فاير بالمعرف مع توصيل فوري.",
            descriptionEn = "Big diamond package for Free Fire via ID with fast delivery.",
            priceEgp = 220.0,
            minLimit = 1,
            maxLimit = 30,
            imageUrl = "",
            isAvailable = true
        ),
        ServiceItem(
            id = "srv_roblox_80",
            nameAr = "روبلوكس (80 روبوكس)",
            nameEn = "Roblox (80 Robux)",
            categoryId = "cat_games",
            categoryAr = "شحن الألعاب",
            categoryEn = "Game Top-up",
            descriptionAr = "بطاقة أو شحن مباشر لحساب روبلوكس 80 Robux.",
            descriptionEn = "Direct Roblox account top-up for 80 Robux.",
            priceEgp = 60.0,
            minLimit = 1,
            maxLimit = 50,
            imageUrl = "",
            isAvailable = true
        ),

        // App Subscriptions Services
        ServiceItem(
            id = "srv_tiktok_coins_70",
            nameAr = "عملات تيك توك (70 عملة)",
            nameEn = "TikTok Coins (70 Coins)",
            categoryId = "cat_apps",
            categoryAr = "شحن التطبيقات",
            categoryEn = "App Subscriptions",
            descriptionAr = "شحن حساب تيك توك بـ 70 عملة لدعم البث المباشر.",
            descriptionEn = "Top up TikTok account with 70 Coins for live stream support.",
            priceEgp = 50.0,
            minLimit = 1,
            maxLimit = 50,
            imageUrl = "",
            isAvailable = true
        ),
        ServiceItem(
            id = "srv_shahid_vip",
            nameAr = "اشتراك شاهد VIP (شهر)",
            nameEn = "Shahid VIP Subscription (1 Month)",
            categoryId = "cat_apps",
            categoryAr = "شحن التطبيقات",
            categoryEn = "App Subscriptions",
            descriptionAr = "اشتراك شهر كامل في منصة شاهد VIP بجودة عالية بدون إعلانات.",
            descriptionEn = "Full month subscription to Shahid VIP in full HD ad-free.",
            priceEgp = 95.0,
            minLimit = 1,
            maxLimit = 10,
            imageUrl = "",
            isAvailable = true
        ),
        ServiceItem(
            id = "srv_telegram_premium",
            nameAr = "تيليجرام بريميوم (3 أشهر)",
            nameEn = "Telegram Premium (3 Months)",
            categoryId = "cat_apps",
            categoryAr = "شحن التطبيقات",
            categoryEn = "App Subscriptions",
            descriptionAr = "تفعيل باقة Telegram Premium الحصرية لمدة 3 أشهر برقمك.",
            descriptionEn = "Activate Telegram Premium subscription for 3 months.",
            priceEgp = 390.0,
            minLimit = 1,
            maxLimit = 5,
            imageUrl = "",
            isAvailable = true
        ),

        // Social Media Services
        ServiceItem(
            id = "srv_insta_followers_1k",
            nameAr = "متابعين انستجرام (1,000 متابع)",
            nameEn = "Instagram Followers (1,000)",
            categoryId = "cat_social",
            categoryAr = "السوشيال ميديا",
            categoryEn = "Social Media",
            descriptionAr = "زيادة 1,000 متابع انستجرام عالي الجودة مع ضمان عدم النقصان.",
            descriptionEn = "High quality 1,000 Instagram followers with drop protection guarantee.",
            priceEgp = 75.0,
            minLimit = 1,
            maxLimit = 20,
            imageUrl = "",
            isAvailable = true
        ),
        ServiceItem(
            id = "srv_fb_likes_1k",
            nameAr = "لايكات فيسبوك (1,000 لايك)",
            nameEn = "Facebook Post Likes (1,000)",
            categoryId = "cat_social",
            categoryAr = "السوشيال ميديا",
            categoryEn = "Social Media",
            descriptionAr = "تزويد 1,000 تفاعل ولايك لمنشورات صفحات وفلايل فيسبوك.",
            descriptionEn = "Boost 1,000 likes/reactions for Facebook posts or pages.",
            priceEgp = 40.0,
            minLimit = 1,
            maxLimit = 50,
            imageUrl = "",
            isAvailable = true
        ),

        // Other Services
        ServiceItem(
            id = "srv_google_play_5",
            nameAr = "بطاقة جوجل بلاي $5 أمريكي",
            nameEn = "Google Play $5 Card (US)",
            categoryId = "cat_other",
            categoryAr = "خدمات أخرى",
            categoryEn = "Other Services",
            descriptionAr = "كود بطاقة هدايا جوجل بلاي متجر أمريكي تسليم فوري.",
            descriptionEn = "Google Play $5 US Store digital gift code instant delivery.",
            priceEgp = 260.0,
            minLimit = 1,
            maxLimit = 10,
            imageUrl = "",
            isAvailable = true
        )
    )

    fun getDefaultPaymentMethods(): List<PaymentMethod> = listOf(
        PaymentMethod(
            id = "pm_vodafone",
            nameAr = "فودافون كاش (Vodafone Cash)",
            nameEn = "Vodafone Cash",
            number = "01147678818",
            iconType = "vodafone",
            instructionsAr = "قم بتحويل المبلغ المطلوبة إلى الرقم أعلاه ثم أرسل لقطة الشاشة في واتساب.",
            instructionsEn = "Transfer the required amount to the number above and send screenshot via WhatsApp."
        ),
        PaymentMethod(
            id = "pm_etisalat",
            nameAr = "اتصالات كاش (Etisalat Cash)",
            nameEn = "Etisalat Cash",
            number = "01147678818",
            iconType = "etisalat",
            instructionsAr = "تحويل كاش إلى رقم اتصالات الموضح، ثم تأكيد المعاملة بلقطة الشاشة.",
            instructionsEn = "Transfer to Etisalat Cash number and confirm with screenshot."
        ),
        PaymentMethod(
            id = "pm_instapay",
            nameAr = "انستا باي (InstaPay)",
            nameEn = "InstaPay",
            number = "01147678818",
            iconType = "instapay",
            instructionsAr = "حوالة فورية عبر تطبيق InstaPay باستخدام رقم الهاتف الموضح.",
            instructionsEn = "Instant transfer via InstaPay app using the phone number shown."
        )
    )

    fun getDefaultStats(): AppStats = AppStats(
        id = "default_stats",
        baseClientsCount = 1211,
        baseTimestamp = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000L), // 10 days ago so it increments smoothly
        ordersCount = 3850,
        servicesCount = 12,
        visitsCount = 24100,
        welcomeMessageAr = "أهلاً بك في NEOVA STORE - متجرك الأول لشحن الألعاب والخدمات الرقمية 🚀",
        welcomeMessageEn = "Welcome to NEOVA STORE - Your #1 hub for game top-ups & digital services 🚀"
    )
}
