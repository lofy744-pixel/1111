package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.CategoryItem
import com.example.models.ServiceItem
import com.example.ui.components.AdminConfirmDialog
import com.example.ui.components.CyberButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberNeonPink
import java.util.UUID

@Composable
fun AdminServicesScreen(
    isArabic: Boolean,
    services: List<ServiceItem>,
    categories: List<CategoryItem>,
    onSaveService: (ServiceItem) -> Unit,
    onDeleteService: (String, String) -> Unit,
    onToggleAvailability: (ServiceItem) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }

    var serviceToEdit by remember { mutableStateOf<ServiceItem?>(null) }
    var isAddDialogOpen by remember { mutableStateOf(false) }
    var serviceToDelete by remember { mutableStateOf<ServiceItem?>(null) }

    val filteredList = services.filter { service ->
        val matchesCategory = selectedCategoryId == null || service.categoryId == selectedCategoryId
        val matchesSearch = searchQuery.isBlank() ||
                service.nameAr.contains(searchQuery, ignoreCase = true) ||
                service.nameEn.contains(searchQuery, ignoreCase = true) ||
                service.descriptionAr.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = CyberNeonCyan
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isArabic) "إدارة الخدمات 🎮" else "Services Management 🎮",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                CyberButton(
                    text = if (isArabic) "إضافة خدمة ➕" else "Add Service ➕",
                    onClick = {
                        serviceToEdit = null
                        isAddDialogOpen = true
                    },
                    primaryColor = CyberNeonPink
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = if (isArabic) "ابحث عن اسم الخدمة..." else "Search service name...",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = CyberNeonCyan
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberNeonCyan,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Categories Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val isSelected = selectedCategoryId == null
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) CyberNeonPink else CyberDarkCard)
                            .border(1.dp, if (isSelected) CyberNeonPink else MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                            .clickable { selectedCategoryId = null }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isArabic) "الكل" else "All",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                items(categories, key = { it.id }) { cat ->
                    val isSelected = selectedCategoryId == cat.id
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) CyberNeonPink else CyberDarkCard)
                            .border(1.dp, if (isSelected) CyberNeonPink else MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                            .clickable { selectedCategoryId = cat.id }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat.getName(isArabic),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Services List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isArabic) "لا توجد خدمات مطابقة" else "No services found",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredList, key = { it.id }) { service ->
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = service.getName(isArabic),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${service.getCategoryName(isArabic)} • ${service.priceEgp} جنيه مصري",
                                            fontSize = 13.sp,
                                            color = CyberNeonCyan,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "الحد الأدنى: ${service.minLimit} | الأقصى: ${service.maxLimit}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    // Switch for Enable/Disable
                                    Switch(
                                        checked = service.isAvailable,
                                        onCheckedChange = { onToggleAvailability(service) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = CyberNeonGreen,
                                            uncheckedThumbColor = Color.Gray,
                                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(onClick = {
                                        serviceToEdit = service
                                        isAddDialogOpen = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = CyberNeonCyan,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isArabic) "تعديل" else "Edit",
                                            color = CyberNeonCyan,
                                            fontSize = 12.sp
                                        )
                                    }

                                    TextButton(onClick = { serviceToDelete = service }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = CyberNeonPink,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isArabic) "حذف" else "Delete",
                                            color = CyberNeonPink,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add / Edit Service Dialog Modal
        if (isAddDialogOpen) {
            ServiceFormDialog(
                isArabic = isArabic,
                categories = categories,
                existingService = serviceToEdit,
                onDismiss = { isAddDialogOpen = false },
                onSave = { updatedService ->
                    onSaveService(updatedService)
                    isAddDialogOpen = false
                }
            )
        }

        // Delete Confirmation Dialog
        if (serviceToDelete != null) {
            AdminConfirmDialog(
                title = if (isArabic) "حذف الخدمة" else "Delete Service",
                message = if (isArabic) "هل أنت تأكد من حذف خدمة (${serviceToDelete?.getName(isArabic)})؟" else "Delete (${serviceToDelete?.nameEn})?",
                confirmText = if (isArabic) "حذف نهائي" else "Delete",
                isDestructive = true,
                onConfirm = {
                    serviceToDelete?.let { onDeleteService(it.id, it.nameAr) }
                    serviceToDelete = null
                },
                onDismiss = { serviceToDelete = null }
            )
        }
    }
}

@Composable
private fun ServiceFormDialog(
    isArabic: Boolean,
    categories: List<CategoryItem>,
    existingService: ServiceItem?,
    onDismiss: () -> Unit,
    onSave: (ServiceItem) -> Unit
) {
    var nameAr by remember { mutableStateOf(existingService?.nameAr ?: "") }
    var nameEn by remember { mutableStateOf(existingService?.nameEn ?: "") }
    var selectedCat by remember { mutableStateOf(categories.find { it.id == existingService?.categoryId } ?: categories.firstOrNull()) }
    var descriptionAr by remember { mutableStateOf(existingService?.descriptionAr ?: "") }
    var descriptionEn by remember { mutableStateOf(existingService?.descriptionEn ?: "") }
    var priceEgpText by remember { mutableStateOf(existingService?.priceEgp?.toString() ?: "50") }
    var minLimitText by remember { mutableStateOf(existingService?.minLimit?.toString() ?: "1") }
    var maxLimitText by remember { mutableStateOf(existingService?.maxLimit?.toString() ?: "1000") }
    var isAvailable by remember { mutableStateOf(existingService?.isAvailable ?: true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existingService == null) (if (isArabic) "إضافة خدمة جديدة ➕" else "Add New Service") else (if (isArabic) "تعديل الخدمة ✏️" else "Edit Service"),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CyberNeonPink
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nameAr,
                    onValueChange = { nameAr = it },
                    label = { Text(if (isArabic) "اسم الخدمة (بالعربي)" else "Name (Arabic)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text(if (isArabic) "اسم الخدمة (بالإنجليزية)" else "Name (English)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Price Field
                OutlinedTextField(
                    value = priceEgpText,
                    onValueChange = { priceEgpText = it },
                    label = { Text(if (isArabic) "السعر (بالجنيه المصري EGP)" else "Price (EGP)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Limits
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = minLimitText,
                        onValueChange = { minLimitText = it },
                        label = { Text(if (isArabic) "الحد الأدنى" else "Min Limit") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = maxLimitText,
                        onValueChange = { maxLimitText = it },
                        label = { Text(if (isArabic) "الحد الأقصى" else "Max Limit") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descriptionAr,
                    onValueChange = { descriptionAr = it },
                    label = { Text(if (isArabic) "الوصف (بالعربي)" else "Description (Arabic)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cat = selectedCat ?: categories.firstOrNull()
                    val newService = ServiceItem(
                        id = existingService?.id ?: "srv_${UUID.randomUUID().toString().take(8)}",
                        nameAr = nameAr.ifBlank { "خدمة جديدة" },
                        nameEn = nameEn.ifBlank { nameAr.ifBlank { "New Service" } },
                        categoryId = cat?.id ?: "cat_games",
                        categoryAr = cat?.nameAr ?: "شحن الألعاب",
                        categoryEn = cat?.nameEn ?: "Games",
                        descriptionAr = descriptionAr,
                        descriptionEn = descriptionEn,
                        priceEgp = priceEgpText.toDoubleOrNull() ?: 50.0,
                        minLimit = minLimitText.toIntOrNull() ?: 1,
                        maxLimit = maxLimitText.toIntOrNull() ?: 1000,
                        isAvailable = isAvailable
                    )
                    onSave(newService)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberNeonPink)
            ) {
                Text(if (isArabic) "حفظ التغييرات" else "Save Changes", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isArabic) "إلغاء" else "Cancel")
            }
        },
        containerColor = CyberDarkCard,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.border(1.dp, CyberNeonCyan, RoundedCornerShape(20.dp))
    )
}
