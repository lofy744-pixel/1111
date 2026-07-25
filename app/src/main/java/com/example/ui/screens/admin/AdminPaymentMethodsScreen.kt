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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhoneAndroid
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.PaymentMethod
import com.example.ui.components.AdminConfirmDialog
import com.example.ui.components.CyberButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonPurple
import java.util.UUID

@Composable
fun AdminPaymentMethodsScreen(
    isArabic: Boolean,
    paymentMethods: List<PaymentMethod>,
    onSavePaymentMethod: (PaymentMethod) -> Unit,
    onDeletePaymentMethod: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var methodToEdit by remember { mutableStateOf<PaymentMethod?>(null) }
    var isAddDialogOpen by remember { mutableStateOf(false) }
    var methodToDelete by remember { mutableStateOf<PaymentMethod?>(null) }

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
                        text = if (isArabic) "وسائل الدفع 💳" else "Payment Methods 💳",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                CyberButton(
                    text = if (isArabic) "وسيلة جديدة ➕" else "New Method ➕",
                    onClick = {
                        methodToEdit = null
                        isAddDialogOpen = true
                    },
                    primaryColor = CyberNeonPink
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (paymentMethods.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isArabic) "لا توجد وسائل دفع مضافة" else "No payment methods available",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(paymentMethods, key = { it.id }) { method ->
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column {
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
                                                .background(CyberNeonPurple.copy(alpha = 0.2f))
                                                .border(1.dp, CyberNeonPurple, RoundedCornerShape(12.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (method.iconType == "vodafone") Icons.Default.PhoneAndroid else Icons.Default.CreditCard,
                                                contentDescription = null,
                                                tint = CyberNeonPurple,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Text(
                                                text = method.getName(isArabic),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "رقم التحويل: ${method.number}",
                                                fontSize = 13.sp,
                                                color = CyberNeonCyan,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }

                                    Row {
                                        IconButton(onClick = {
                                            methodToEdit = method
                                            isAddDialogOpen = true
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit",
                                                tint = CyberNeonCyan
                                            )
                                        }

                                        IconButton(onClick = { methodToDelete = method }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = CyberNeonPink
                                            )
                                        }
                                    }
                                }

                                if (method.getInstructions(isArabic).isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = method.getInstructions(isArabic),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add/Edit Dialog
        if (isAddDialogOpen) {
            PaymentMethodDialog(
                isArabic = isArabic,
                existingMethod = methodToEdit,
                onDismiss = { isAddDialogOpen = false },
                onSave = { updated ->
                    onSavePaymentMethod(updated)
                    isAddDialogOpen = false
                }
            )
        }

        // Delete Dialog
        if (methodToDelete != null) {
            AdminConfirmDialog(
                title = if (isArabic) "حذف وسيلة الدفع" else "Delete Payment Method",
                message = if (isArabic) "هل أنت تأكد من حذف (${methodToDelete?.getName(isArabic)})؟" else "Delete (${methodToDelete?.nameEn})?",
                confirmText = if (isArabic) "حذف" else "Delete",
                isDestructive = true,
                onConfirm = {
                    methodToDelete?.let { onDeletePaymentMethod(it.id, it.nameAr) }
                    methodToDelete = null
                },
                onDismiss = { methodToDelete = null }
            )
        }
    }
}

@Composable
private fun PaymentMethodDialog(
    isArabic: Boolean,
    existingMethod: PaymentMethod?,
    onDismiss: () -> Unit,
    onSave: (PaymentMethod) -> Unit
) {
    var nameAr by remember { mutableStateOf(existingMethod?.nameAr ?: "") }
    var nameEn by remember { mutableStateOf(existingMethod?.nameEn ?: "") }
    var number by remember { mutableStateOf(existingMethod?.number ?: "") }
    var iconType by remember { mutableStateOf(existingMethod?.iconType ?: "vodafone") }
    var instructionsAr by remember { mutableStateOf(existingMethod?.instructionsAr ?: "") }
    var instructionsEn by remember { mutableStateOf(existingMethod?.instructionsEn ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existingMethod == null) (if (isArabic) "إضافة وسيلة دفع 💳" else "Add Payment Method") else (if (isArabic) "تعديل وسيلة الدفع ✏️" else "Edit Payment Method"),
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
                    label = { Text(if (isArabic) "الاسم (بالعربي)" else "Name (Arabic)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text(if (isArabic) "الاسم (بالإنجليزية)" else "Name (English)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text(if (isArabic) "رقم المحفظة / الحساب" else "Wallet / Account Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = instructionsAr,
                    onValueChange = { instructionsAr = it },
                    label = { Text(if (isArabic) "تعليمات التحويل (بالعربي)" else "Instructions (Arabic)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val method = PaymentMethod(
                        id = existingMethod?.id ?: "pm_${UUID.randomUUID().toString().take(8)}",
                        nameAr = nameAr.ifBlank { "وسيلة دفع" },
                        nameEn = nameEn.ifBlank { nameAr.ifBlank { "Payment Method" } },
                        number = number,
                        iconType = iconType,
                        instructionsAr = instructionsAr,
                        instructionsEn = instructionsEn
                    )
                    onSave(method)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberNeonPink)
            ) {
                Text(if (isArabic) "حفظ" else "Save", fontWeight = FontWeight.Bold)
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
