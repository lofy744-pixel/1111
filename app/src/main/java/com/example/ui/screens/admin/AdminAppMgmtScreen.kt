package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.models.AppSettings
import com.example.models.BannerItem
import com.example.ui.components.AdminConfirmDialog
import com.example.ui.components.CyberButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonPink
import java.util.UUID

@Composable
fun AdminAppMgmtScreen(
    isArabic: Boolean,
    banners: List<BannerItem>,
    appSettings: AppSettings,
    onSaveBanner: (BannerItem) -> Unit,
    onDeleteBanner: (String) -> Unit,
    onSaveSettings: (AppSettings) -> Unit,
    onBackClick: () -> Unit
) {
    var orderWhatsappNumber by remember { mutableStateOf(appSettings.orderWhatsappNumber) }
    var supportWhatsappNumber by remember { mutableStateOf(appSettings.supportWhatsappNumber) }
    var welcomeMsgAr by remember { mutableStateOf(appSettings.welcomeMessageAr) }
    var welcomeMsgEn by remember { mutableStateOf(appSettings.welcomeMessageEn) }

    var isAddBannerOpen by remember { mutableStateOf(false) }
    var bannerToDelete by remember { mutableStateOf<BannerItem?>(null) }

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
                    text = if (isArabic) "إدارة الواجهة والبنرات 📱" else "Banners & Contact Mgmt 📱",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Info Section
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = CyberNeonCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isArabic) "بيانات الدعم والواتساب 📞" else "WhatsApp & Support 📞",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = orderWhatsappNumber,
                        onValueChange = { orderWhatsappNumber = it },
                        label = { Text(if (isArabic) "رقم واتساب استقبال الطلبات" else "Orders WhatsApp Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = supportWhatsappNumber,
                        onValueChange = { supportWhatsappNumber = it },
                        label = { Text(if (isArabic) "رقم هاتف الدعم الفني" else "Support Phone") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = welcomeMsgAr,
                        onValueChange = { welcomeMsgAr = it },
                        label = { Text(if (isArabic) "رسالة الترحيب (بالعربي)" else "Welcome Message (Arabic)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    CyberButton(
                        text = if (isArabic) "حفظ بيانات الاتصال 💾" else "Save Contact Info 💾",
                        onClick = {
                            val updated = appSettings.copy(
                                orderWhatsappNumber = orderWhatsappNumber,
                                supportWhatsappNumber = supportWhatsappNumber,
                                welcomeMessageAr = welcomeMsgAr,
                                welcomeMessageEn = welcomeMsgEn
                            )
                            onSaveSettings(updated)
                        },
                        primaryColor = CyberNeonCyan,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Banners Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = CyberNeonPink,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isArabic) "بنرات العروض والخدمات 🖼️" else "Promotional Banners 🖼️",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                CyberButton(
                    text = if (isArabic) "إضافة بنر ➕" else "Add Banner ➕",
                    onClick = { isAddBannerOpen = true },
                    primaryColor = CyberNeonPink,
                    modifier = Modifier.width(130.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (banners.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isArabic) "لا توجد بنرات مضافة حالياً" else "No banners found",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                banners.forEach { banner ->
                    GlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (banner.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = banner.imageUrl,
                                    contentDescription = banner.getTitle(isArabic),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(70.dp, 50.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = banner.getTitle(isArabic),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (banner.getSubtitle(isArabic).isNotBlank()) {
                                    Text(
                                        text = banner.getSubtitle(isArabic),
                                        fontSize = 12.sp,
                                        color = CyberNeonCyan
                                    )
                                }
                            }

                            IconButton(onClick = { bannerToDelete = banner }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = CyberNeonPink
                                )
                            }
                        }
                    }
                }
            }
        }

        // Add Banner Dialog
        if (isAddBannerOpen) {
            BannerEditDialog(
                isArabic = isArabic,
                onDismiss = { isAddBannerOpen = false },
                onSave = { newBanner ->
                    onSaveBanner(newBanner)
                    isAddBannerOpen = false
                }
            )
        }

        // Delete Banner Dialog
        if (bannerToDelete != null) {
            AdminConfirmDialog(
                title = if (isArabic) "حذف البنر" else "Delete Banner",
                message = if (isArabic) "هل أنت تأكد من حذف البنر '${bannerToDelete?.getTitle(isArabic)}'؟" else "Delete '${bannerToDelete?.getTitle(isArabic)}'?",
                confirmText = if (isArabic) "حذف" else "Delete",
                isDestructive = true,
                onConfirm = {
                    bannerToDelete?.let { onDeleteBanner(it.id) }
                    bannerToDelete = null
                },
                onDismiss = { bannerToDelete = null }
            )
        }
    }
}

@Composable
private fun BannerEditDialog(
    isArabic: Boolean,
    onDismiss: () -> Unit,
    onSave: (BannerItem) -> Unit
) {
    var titleAr by remember { mutableStateOf("") }
    var subtitleAr by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var targetCategoryId by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isArabic) "إضافة بنر إعلاني جديد 🖼️" else "Add New Banner 🖼️",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CyberNeonPink
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = titleAr,
                    onValueChange = { titleAr = it },
                    label = { Text(if (isArabic) "العنوان الرئيسي (عربي)" else "Banner Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = subtitleAr,
                    onValueChange = { subtitleAr = it },
                    label = { Text(if (isArabic) "العنوان الفرعي" else "Subtitle") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text(if (isArabic) "رابط الصورة (URL)" else "Image URL") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = targetCategoryId,
                    onValueChange = { targetCategoryId = it },
                    label = { Text(if (isArabic) "معرف القسم المرتبط (اختياري)" else "Target Category ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (titleAr.isNotBlank()) {
                        val banner = BannerItem(
                            id = UUID.randomUUID().toString(),
                            titleAr = titleAr,
                            titleEn = titleAr,
                            subtitleAr = subtitleAr,
                            subtitleEn = subtitleAr,
                            imageUrl = imageUrl,
                            targetCategoryId = targetCategoryId
                        )
                        onSave(banner)
                    }
                }
            ) {
                Text(
                    text = if (isArabic) "حفظ البنر" else "Save Banner",
                    fontWeight = FontWeight.Bold,
                    color = CyberNeonCyan
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = if (isArabic) "إلغاء" else "Cancel", color = Color.Gray)
            }
        },
        containerColor = CyberDarkCard,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.border(1.dp, CyberNeonPink.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
    )
}
