package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.OrderRequest
import com.example.models.ServiceItem
import com.example.ui.components.CyberButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonPurple
import com.example.utils.WhatsAppHelper

@Composable
fun OrderCreateScreen(
    service: ServiceItem?,
    isArabic: Boolean,
    submittedOrder: OrderRequest?,
    submissionError: String? = null,
    onBackClick: () -> Unit,
    onSubmitOrder: (customerName: String, phone: String, accountIdOrLink: String, quantity: Int, notes: String) -> Unit,
    onDismissModal: () -> Unit
) {
    if (service == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = if (isArabic) "يرجى اختيار خدمة أولاً" else "Please select a service first")
        }
        return
    }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var customerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var accountIdOrLink by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(service.minLimit) }
    var notes by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val totalPrice = service.priceEgp * quantity

    // Submission Confirmation Modal Notice
    if (submittedOrder != null) {
        AlertDialog(
            onDismissRequest = onDismissModal,
            confirmButton = {
                TextButton(
                    onClick = {
                        WhatsAppHelper.sendOrderWhatsApp(context, submittedOrder)
                        onDismissModal()
                        onBackClick()
                    }
                ) {
                    Text(
                        text = if (isArabic) "متابعة إلى واتساب 🚀" else "Proceed to WhatsApp 🚀",
                        color = CyberNeonPink,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = CyberNeonCyan,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isArabic) "تم إنشاء الطلب #${submittedOrder.orderId}" else "Order Created #${submittedOrder.orderId}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = if (isArabic)
                            "تم حفظ طلبك بنجاح. سيتم فتح واتساب لإرسال التفاصيل."
                        else
                            "Your order has been recorded successfully. WhatsApp will open now.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberNeonPink.copy(alpha = 0.15f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = if (isArabic)
                                "يرجى إرسال لقطة شاشة التحويل يدويًا في نفس محادثة واتساب لإكمال الطلب."
                            else
                                "Please manually send the transfer screenshot in the same WhatsApp chat to complete the order.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberNeonPink,
                            lineHeight = 18.sp
                        )
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = CyberNeonPink
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (isArabic) "إنشاء طلب جديد" else "Create New Order",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Service Overview Card
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(CyberNeonPink.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = CyberNeonPink
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = service.getName(isArabic),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${service.priceEgp} ${if (isArabic) "ج.م لكل وحدة" else "EGP per unit"}",
                        fontSize = 13.sp,
                        color = CyberNeonCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Fields
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Customer Name
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text(if (isArabic) "اسم العميل *" else "Customer Name *") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CyberNeonPink) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = textFieldColors(),
                singleLine = true
            )

            // Phone Number
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(if (isArabic) "رقم الهاتف *" else "Phone Number *") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = CyberNeonPink) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(16.dp),
                colors = textFieldColors(),
                singleLine = true
            )

            // Account Link or ID
            OutlinedTextField(
                value = accountIdOrLink,
                onValueChange = { accountIdOrLink = it },
                label = { Text(if (isArabic) "رابط الحساب أو الـ ID *" else "Account Link or ID *") },
                leadingIcon = { Icon(Icons.Default.Link, contentDescription = null, tint = CyberNeonPink) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = textFieldColors(),
                singleLine = true
            )

            // Quantity Selection
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = if (isArabic) "الكمية المطلوبة *" else "Quantity *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = {
                                if (quantity > service.minLimit) quantity--
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(CyberNeonPink.copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Minus", tint = CyberNeonPink)
                        }

                        Text(
                            text = "$quantity",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CyberNeonCyan
                        )

                        IconButton(
                            onClick = {
                                if (quantity < service.maxLimit) quantity++
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(CyberNeonPink.copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Plus", tint = CyberNeonPink)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isArabic) "الحد الأدنى: ${service.minLimit} | الحد الأقصى: ${service.maxLimit}"
                        else "Min: ${service.minLimit} | Max: ${service.maxLimit}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text(if (isArabic) "ملاحظات إضافية (اختياري)" else "Notes (Optional)") },
                leadingIcon = { Icon(Icons.Default.Note, contentDescription = null, tint = CyberNeonPink) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = textFieldColors(),
                maxLines = 3
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Total Price Calculation Box
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderGradient = listOf(CyberNeonCyan, CyberNeonPink)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "الإجمالي الكلي:" else "Total Price:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$totalPrice ${if (isArabic) "ج.م" else "EGP"}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CyberNeonPink
                )
            }
        }

        val activeError = submissionError ?: errorMessage
        if (activeError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = activeError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Order Button
        CyberButton(
            text = if (isArabic) "إرسال الطلب عبر واتساب 🚀" else "Submit Order via WhatsApp 🚀",
            onClick = {
                if (customerName.isBlank()) {
                    errorMessage = if (isArabic) "يرجى كتابة اسم العميل" else "Please enter customer name"
                    return@CyberButton
                }
                if (phone.isBlank()) {
                    errorMessage = if (isArabic) "يرجى كتابة رقم الهاتف" else "Please enter phone number"
                    return@CyberButton
                }
                if (accountIdOrLink.isBlank()) {
                    errorMessage = if (isArabic) "يرجى كتابة رابط الحساب أو ID" else "Please enter account ID/link"
                    return@CyberButton
                }

                errorMessage = null
                onSubmitOrder(customerName, phone, accountIdOrLink, quantity, notes)
            },
            icon = Icons.Default.Send,
            gradientColors = listOf(CyberNeonPink, CyberNeonPurple)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CyberNeonPink,
    unfocusedBorderColor = CyberNeonCyan.copy(alpha = 0.4f),
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
)
