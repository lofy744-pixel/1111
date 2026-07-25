package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.AppSettings
import com.example.ui.components.AdminConfirmDialog
import com.example.ui.components.CyberButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberNeonPink

@Composable
fun AdminSettingsScreen(
    isArabic: Boolean,
    appSettings: AppSettings,
    onSaveSettings: (AppSettings) -> Unit,
    onReloadData: () -> Unit,
    onBackClick: () -> Unit
) {
    var appName by remember { mutableStateOf(appSettings.appName) }
    var welcomeMessageAr by remember { mutableStateOf(appSettings.welcomeMessageAr) }
    var maintenanceMode by remember { mutableStateOf(appSettings.maintenanceMode) }
    var maintenanceMessageAr by remember { mutableStateOf(appSettings.maintenanceMessageAr) }

    var showSyncConfirm by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = CyberNeonCyan
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isArabic) "إعدادات التطبيق والصيانة ⚙️" else "App Settings & Maintenance ⚙️",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // General Settings Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = CyberNeonPink,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isArabic) "إعدادات الهوية والتطبيق 🎨" else "Branding & Config 🎨",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = appName,
                        onValueChange = { appName = it },
                        label = { Text(if (isArabic) "اسم التطبيق" else "App Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = welcomeMessageAr,
                        onValueChange = { welcomeMessageAr = it },
                        label = { Text(if (isArabic) "رسالة الترحيب" else "Welcome Message") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CyberButton(
                        text = if (isArabic) "حفظ الإعدادات 💾" else "Save Settings 💾",
                        onClick = {
                            val updated = appSettings.copy(
                                appName = appName,
                                welcomeMessageAr = welcomeMessageAr,
                                maintenanceMode = maintenanceMode,
                                maintenanceMessageAr = maintenanceMessageAr
                            )
                            onSaveSettings(updated)
                        },
                        primaryColor = CyberNeonPink,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Maintenance Mode Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = null,
                                tint = if (maintenanceMode) CyberNeonPink else CyberNeonGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isArabic) "وضع الصيانة 🛠️" else "Maintenance Mode 🛠️",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (maintenanceMode)
                                        (if (isArabic) "التطبيق متوقف حالياً للصيانة" else "App is under maintenance")
                                    else
                                        (if (isArabic) "التطبيق يعمل بشكل طبيعي" else "App is online"),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = maintenanceMode,
                            onCheckedChange = {
                                maintenanceMode = it
                                val updated = appSettings.copy(maintenanceMode = it)
                                onSaveSettings(updated)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CyberNeonPink,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = CyberNeonGreen
                            )
                        )
                    }

                    if (maintenanceMode) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = maintenanceMessageAr,
                            onValueChange = { maintenanceMessageAr = it },
                            label = { Text(if (isArabic) "رسالة التوقف للصيانة" else "Maintenance Message") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Data Sync & Cloud Backup
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = null,
                            tint = CyberNeonCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isArabic) "مزامنة البيانات والنسخ الاحتياطي ☁️" else "Cloud Sync & Backup ☁️",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isArabic)
                            "تسمح لك هذه الوظيفة بإعادة سحب جميع البيانات مباشرة من قاعدة بيانات Firebase Firestore وتحديث التخزين المحلي."
                        else
                            "Resync all remote data directly from Firebase Firestore.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    CyberButton(
                        text = if (isArabic) "إعادة مزامنة البيانات الآن 🔄" else "Resync Firebase Data 🔄",
                        onClick = { showSyncConfirm = true },
                        primaryColor = CyberNeonCyan,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        if (showSyncConfirm) {
            AdminConfirmDialog(
                title = if (isArabic) "مزامنة البيانات" else "Resync Data",
                message = if (isArabic) "هل ترغب في جلب وتحديث كامل البيانات من Firebase Firestore الآن؟" else "Resync from Firestore?",
                confirmText = if (isArabic) "بدء المزامنة" else "Sync Now",
                onConfirm = {
                    showSyncConfirm = false
                    onReloadData()
                },
                onDismiss = { showSyncConfirm = false }
            )
        }
    }
}
