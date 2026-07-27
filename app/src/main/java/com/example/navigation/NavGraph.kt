package com.example.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.models.ServiceItem
import com.example.ui.components.AdminNotificationBanner
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopAppBar
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.MyOrdersScreen
import com.example.ui.screens.OrderCreateScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.ServiceDetailsScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.admin.AdminActivityLogsScreen
import com.example.ui.screens.admin.AdminAppMgmtScreen
import com.example.ui.screens.admin.AdminCategoriesScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminLoginScreen
import com.example.ui.screens.admin.AdminOrdersScreen
import com.example.ui.screens.admin.AdminPaymentMethodsScreen
import com.example.ui.screens.admin.AdminProfileScreen
import com.example.ui.screens.admin.AdminServicesScreen
import com.example.ui.screens.admin.AdminSettingsScreen
import com.example.viewmodels.AdminViewModel
import com.example.viewmodels.MainViewModel

object Routes {
    const val SPLASH = "splash"
    const val MAIN = "main"
    const val SERVICE_DETAILS = "service_details"
    const val ORDER_CREATE = "order_create"

    // Admin Routes
    const val ADMIN_LOGIN = "admin_login"
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_SERVICES = "admin_services"
    const val ADMIN_CATEGORIES = "admin_categories"
    const val ADMIN_ORDERS = "admin_orders"
    const val ADMIN_PAYMENTS = "admin_payments"
    const val ADMIN_APP_MGMT = "admin_app_mgmt"
    const val ADMIN_SETTINGS = "admin_settings"
    const val ADMIN_PROFILE = "admin_profile"
    const val ADMIN_ACTIVITY_LOGS = "admin_activity_logs"
}

@Composable
fun NavGraph(
    viewModel: MainViewModel,
    adminViewModel: AdminViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isArabic by viewModel.isArabic.collectAsState()

    val isAdminLoggedIn by adminViewModel.isAdminLoggedIn.collectAsState()
    val adminNotificationMsg by adminViewModel.notificationMessage.collectAsState()
    val adminNotificationType by adminViewModel.notificationType.collectAsState()

    val layoutDirection = if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Routes.SPLASH,
                enterTransition = { fadeIn(animationSpec = tween(180)) },
                exitTransition = { fadeOut(animationSpec = tween(180)) },
                popEnterTransition = { fadeIn(animationSpec = tween(180)) },
                popExitTransition = { fadeOut(animationSpec = tween(180)) }
            ) {
                // Splash Screen
                composable(Routes.SPLASH) {
                    SplashScreen(
                        isArabic = isArabic,
                        onSplashFinished = {
                            navController.navigate(Routes.MAIN) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        }
                    )
                }

                // Main Container with Bottom Navigation
                composable(Routes.MAIN) {
                    MainContainer(
                        viewModel = viewModel,
                        onNavigateToDetails = { service ->
                            viewModel.selectService(service)
                            navController.navigate(Routes.SERVICE_DETAILS)
                        },
                        onNavigateToOrderCreate = { service ->
                            viewModel.selectService(service)
                            navController.navigate(Routes.ORDER_CREATE)
                        },
                        onOpenAdmin = {
                            if (isAdminLoggedIn) {
                                navController.navigate(Routes.ADMIN_DASHBOARD)
                            } else {
                                navController.navigate(Routes.ADMIN_LOGIN)
                            }
                        }
                    )
                }

                // Service Details Screen
                composable(Routes.SERVICE_DETAILS) {
                    val selectedService by viewModel.selectedService.collectAsState()
                    ServiceDetailsScreen(
                        service = selectedService,
                        isArabic = isArabic,
                        onBackClick = { navController.popBackStack() },
                        onCreateOrderClick = { service ->
                            viewModel.selectService(service)
                            navController.navigate(Routes.ORDER_CREATE)
                        }
                    )
                }

                // Order Create Screen
                composable(Routes.ORDER_CREATE) {
                    val selectedService by viewModel.selectedService.collectAsState()
                    val submittedOrder by viewModel.orderSubmissionResult.collectAsState()
                    val submissionError by viewModel.orderErrorMessage.collectAsState()

                    OrderCreateScreen(
                        service = selectedService,
                        isArabic = isArabic,
                        submittedOrder = submittedOrder,
                        submissionError = submissionError,
                        onBackClick = { navController.popBackStack() },
                        onSubmitOrder = { name, phone, idLink, qty, notes ->
                            if (selectedService != null) {
                                viewModel.submitOrder(
                                    service = selectedService!!,
                                    customerName = name,
                                    phone = phone,
                                    accountIdOrLink = idLink,
                                    quantity = qty,
                                    notes = notes
                                )
                            }
                        },
                        onDismissModal = {
                            viewModel.clearOrderSubmissionResult()
                        }
                    )
                }

                // ADMIN SCREENS
                // 1. Admin Login
                composable(Routes.ADMIN_LOGIN) {
                    AdminLoginScreen(
                        isArabic = isArabic,
                        onLoginClick = { user, pass, rememberMe ->
                            adminViewModel.login(user, pass, rememberMe) { success, _ ->
                                if (success) {
                                    navController.navigate(Routes.ADMIN_DASHBOARD) {
                                        popUpTo(Routes.ADMIN_LOGIN) { inclusive = true }
                                    }
                                }
                            }
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 2. Admin Dashboard
                composable(Routes.ADMIN_DASHBOARD) {
                    val orders by adminViewModel.orders.collectAsState()
                    val services by adminViewModel.allServices.collectAsState()
                    val categories by adminViewModel.categories.collectAsState()
                    val appStats by viewModel.appStats.collectAsState()
                    val logs by adminViewModel.activityLogs.collectAsState()

                    AdminDashboardScreen(
                        isArabic = isArabic,
                        orders = orders,
                        services = services,
                        categories = categories,
                        appStats = appStats,
                        logs = logs,
                        onNavigate = { route -> navController.navigate(route) },
                        onReloadData = { adminViewModel.reloadFirebaseData() },
                        onLogout = {
                            adminViewModel.logout()
                            navController.navigate(Routes.MAIN) {
                                popUpTo(Routes.ADMIN_DASHBOARD) { inclusive = true }
                            }
                        }
                    )
                }

                // 3. Admin Services
                composable(Routes.ADMIN_SERVICES) {
                    val services by adminViewModel.allServices.collectAsState()
                    val categories by adminViewModel.categories.collectAsState()

                    AdminServicesScreen(
                        isArabic = isArabic,
                        services = services,
                        categories = categories,
                        onSaveService = { adminViewModel.saveService(it) },
                        onDeleteService = { id, name -> adminViewModel.deleteService(id, name) },
                        onToggleAvailability = { adminViewModel.toggleServiceAvailability(it) },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 4. Admin Categories
                composable(Routes.ADMIN_CATEGORIES) {
                    val categories by adminViewModel.categories.collectAsState()

                    AdminCategoriesScreen(
                        isArabic = isArabic,
                        categories = categories,
                        onSaveCategory = { adminViewModel.saveCategory(it) },
                        onDeleteCategory = { id, name -> adminViewModel.deleteCategory(id, name) },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 5. Admin Orders
                composable(Routes.ADMIN_ORDERS) {
                    val orders by adminViewModel.orders.collectAsState()

                    AdminOrdersScreen(
                        isArabic = isArabic,
                        orders = orders,
                        onUpdateOrderStatus = { id, status -> adminViewModel.updateOrderStatus(id, status) },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 6. Admin Payments
                composable(Routes.ADMIN_PAYMENTS) {
                    val paymentMethods by adminViewModel.paymentMethods.collectAsState()

                    AdminPaymentMethodsScreen(
                        isArabic = isArabic,
                        paymentMethods = paymentMethods,
                        onSavePaymentMethod = { adminViewModel.savePaymentMethod(it) },
                        onDeletePaymentMethod = { id, name -> adminViewModel.deletePaymentMethod(id, name) },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 7. Admin App Mgmt & Banners
                composable(Routes.ADMIN_APP_MGMT) {
                    val banners by adminViewModel.banners.collectAsState()
                    val appSettings by adminViewModel.appSettings.collectAsState()

                    AdminAppMgmtScreen(
                        isArabic = isArabic,
                        banners = banners,
                        appSettings = appSettings,
                        onSaveBanner = { adminViewModel.saveBanner(it) },
                        onDeleteBanner = { adminViewModel.deleteBanner(it) },
                        onSaveSettings = { adminViewModel.saveAppSettings(it) },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 8. Admin Settings & Maintenance
                composable(Routes.ADMIN_SETTINGS) {
                    val appSettings by adminViewModel.appSettings.collectAsState()

                    AdminSettingsScreen(
                        isArabic = isArabic,
                        appSettings = appSettings,
                        onSaveSettings = { adminViewModel.saveAppSettings(it) },
                        onReloadData = { adminViewModel.reloadFirebaseData() },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 9. Admin Profile
                composable(Routes.ADMIN_PROFILE) {
                    val creds by adminViewModel.adminCredentials.collectAsState()

                    AdminProfileScreen(
                        isArabic = isArabic,
                        credentials = creds,
                        onUpdateProfile = { currentPass, newUsername, newPassword, onResult ->
                            adminViewModel.updateAdminProfile(currentPass, newUsername, newPassword, onResult)
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 10. Admin Activity Logs
                composable(Routes.ADMIN_ACTIVITY_LOGS) {
                    val logs by adminViewModel.activityLogs.collectAsState()

                    AdminActivityLogsScreen(
                        isArabic = isArabic,
                        logs = logs,
                        onClearLogs = { adminViewModel.clearLogs() },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            // Notification Overlay Banner
            AdminNotificationBanner(
                message = adminNotificationMsg,
                type = adminNotificationType,
                onDismiss = { adminViewModel.clearNotification() }
            )
        }
    }
}

@Composable
fun MainContainer(
    viewModel: MainViewModel,
    onNavigateToDetails: (ServiceItem) -> Unit,
    onNavigateToOrderCreate: (ServiceItem) -> Unit,
    onOpenAdmin: () -> Unit
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isArabic by viewModel.isArabic.collectAsState()
    val services by viewModel.services.collectAsState()
    val filteredServices by viewModel.filteredServices.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val banners by viewModel.banners.collectAsState()
    val paymentMethods by viewModel.paymentMethods.collectAsState()
    val appStats by viewModel.appStats.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                isDarkMode = isDarkMode,
                isArabic = isArabic,
                onToggleDarkMode = { viewModel.toggleDarkMode() },
                onToggleLanguage = {
                    viewModel.setLanguage(if (isArabic) "en" else "ar")
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                isArabic = isArabic,
                onTabSelected = { tab ->
                    if (currentRoute != tab.route) {
                        bottomNavController.navigate(tab.route) {
                            popUpTo(bottomNavController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = "home",
                enterTransition = { fadeIn(animationSpec = tween(180)) },
                exitTransition = { fadeOut(animationSpec = tween(180)) },
                popEnterTransition = { fadeIn(animationSpec = tween(180)) },
                popExitTransition = { fadeOut(animationSpec = tween(180)) }
            ) {
                // Home Tab
                composable("home") {
                    HomeScreen(
                        banners = banners,
                        appStats = appStats,
                        isArabic = isArabic,
                        onNavigateToServices = {
                            bottomNavController.navigate("services")
                        },
                        onCategorySelected = { catId ->
                            viewModel.selectedCategoryId.value = catId
                            bottomNavController.navigate("services")
                        }
                    )
                }

                // Services Tab
                composable("services") {
                    ServicesScreen(
                        services = filteredServices,
                        categories = categories,
                        searchQuery = searchQuery,
                        selectedCategoryId = selectedCategoryId,
                        isArabic = isArabic,
                        onSearchQueryChange = { query ->
                            viewModel.searchQuery.value = query
                        },
                        onCategorySelect = { catId ->
                            viewModel.selectedCategoryId.value = catId
                        },
                        onServiceClick = onNavigateToDetails,
                        onOrderNowClick = onNavigateToOrderCreate
                    )
                }

                // Orders Tab
                composable("orders") {
                    MyOrdersScreen(
                        orders = orders,
                        isArabic = isArabic
                    )
                }

                // Payment Tab
                composable("payment") {
                    PaymentScreen(
                        paymentMethods = paymentMethods,
                        isArabic = isArabic
                    )
                }

                // More Tab
                composable("more") {
                    MoreScreen(
                        isDarkMode = isDarkMode,
                        isArabic = isArabic,
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        onToggleLanguage = {
                            viewModel.setLanguage(if (isArabic) "en" else "ar")
                        },
                        onAdminClick = onOpenAdmin
                    )
                }
            }
        }
    }
}
