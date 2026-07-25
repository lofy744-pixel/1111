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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.CategoryItem
import com.example.ui.components.AdminConfirmDialog
import com.example.ui.components.CyberButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonPurple
import java.util.UUID

@Composable
fun AdminCategoriesScreen(
    isArabic: Boolean,
    categories: List<CategoryItem>,
    onSaveCategory: (CategoryItem) -> Unit,
    onDeleteCategory: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var categoryToEdit by remember { mutableStateOf<CategoryItem?>(null) }
    var isAddDialogOpen by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<CategoryItem?>(null) }

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
                        text = if (isArabic) "إدارة الأقسام 🗂️" else "Categories Management 🗂️",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                CyberButton(
                    text = if (isArabic) "قسم جديد ➕" else "New Category ➕",
                    onClick = {
                        categoryToEdit = null
                        isAddDialogOpen = true
                    },
                    primaryColor = CyberNeonPink
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (categories.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isArabic) "لا توجد أقسام معرفة" else "No categories available",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories, key = { it.id }) { category ->
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(CyberNeonPink.copy(alpha = 0.15f))
                                            .border(1.dp, CyberNeonPink, RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when (category.iconName) {
                                                "gamepad" -> Icons.Default.Gamepad
                                                "apps" -> Icons.Default.Apps
                                                "share" -> Icons.Default.Share
                                                "fire" -> Icons.Default.LocalFireDepartment
                                                "star" -> Icons.Default.Star
                                                "shield" -> Icons.Default.Shield
                                                else -> Icons.Default.Widgets
                                            },
                                            contentDescription = null,
                                            tint = CyberNeonPink,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column {
                                        Text(
                                            text = category.getName(isArabic),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "الترتيب: #${category.sortOrder} | ID: ${category.id}",
                                            fontSize = 11.sp,
                                            color = CyberNeonCyan
                                        )
                                    }
                                }

                                Row {
                                    IconButton(onClick = {
                                        categoryToEdit = category
                                        isAddDialogOpen = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = CyberNeonCyan
                                        )
                                    }

                                    IconButton(onClick = { categoryToDelete = category }) {
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
            }
        }

        // Category Add/Edit Dialog
        if (isAddDialogOpen) {
            CategoryFormDialog(
                isArabic = isArabic,
                existingCategory = categoryToEdit,
                onDismiss = { isAddDialogOpen = false },
                onSave = { newCategory ->
                    onSaveCategory(newCategory)
                    isAddDialogOpen = false
                }
            )
        }

        // Delete Dialog
        if (categoryToDelete != null) {
            AdminConfirmDialog(
                title = if (isArabic) "حذف القسم" else "Delete Category",
                message = if (isArabic) "هل أنت متاكد من حذف قسم (${categoryToDelete?.getName(isArabic)})؟" else "Delete (${categoryToDelete?.nameEn})?",
                confirmText = if (isArabic) "حذف" else "Delete",
                isDestructive = true,
                onConfirm = {
                    categoryToDelete?.let { onDeleteCategory(it.id, it.nameAr) }
                    categoryToDelete = null
                },
                onDismiss = { categoryToDelete = null }
            )
        }
    }
}

@Composable
private fun CategoryFormDialog(
    isArabic: Boolean,
    existingCategory: CategoryItem?,
    onDismiss: () -> Unit,
    onSave: (CategoryItem) -> Unit
) {
    var nameAr by remember { mutableStateOf(existingCategory?.nameAr ?: "") }
    var nameEn by remember { mutableStateOf(existingCategory?.nameEn ?: "") }
    var iconName by remember { mutableStateOf(existingCategory?.iconName ?: "gamepad") }
    var sortOrderText by remember { mutableStateOf(existingCategory?.sortOrder?.toString() ?: "1") }

    val iconList = listOf("gamepad", "apps", "share", "widgets", "fire", "star", "shield")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existingCategory == null) (if (isArabic) "إضافة قسم جديد ➕" else "Add New Category") else (if (isArabic) "تعديل القسم ✏️" else "Edit Category"),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CyberNeonPink
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = nameAr,
                    onValueChange = { nameAr = it },
                    label = { Text(if (isArabic) "اسم القسم (بالعربي)" else "Category Name (Arabic)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text(if (isArabic) "اسم القسم (بالإنجليزية)" else "Category Name (English)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = sortOrderText,
                    onValueChange = { sortOrderText = it },
                    label = { Text(if (isArabic) "ترتيب العرض (1, 2, 3...)" else "Sort Order") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isArabic) "اختر أيقونة القسم:" else "Choose Icon:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    iconList.forEach { iconKey ->
                        val isSelected = iconName == iconKey
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CyberNeonPink else CyberDarkCard)
                                .border(1.dp, if (isSelected) CyberNeonPink else MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                                .clickable { iconName = iconKey },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (iconKey) {
                                    "gamepad" -> Icons.Default.Gamepad
                                    "apps" -> Icons.Default.Apps
                                    "share" -> Icons.Default.Share
                                    "fire" -> Icons.Default.LocalFireDepartment
                                    "star" -> Icons.Default.Star
                                    "shield" -> Icons.Default.Shield
                                    else -> Icons.Default.Widgets
                                },
                                contentDescription = null,
                                tint = if (isSelected) Color.White else CyberNeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val category = CategoryItem(
                        id = existingCategory?.id ?: "cat_${UUID.randomUUID().toString().take(8)}",
                        nameAr = nameAr.ifBlank { "قسم جديد" },
                        nameEn = nameEn.ifBlank { nameAr.ifBlank { "New Category" } },
                        iconName = iconName,
                        sortOrder = sortOrderText.toIntOrNull() ?: 1
                    )
                    onSave(category)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberNeonPink)
            ) {
                Text(if (isArabic) "حفظ القسم" else "Save Category", fontWeight = FontWeight.Bold)
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
