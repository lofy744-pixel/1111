package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.AdminCredentials
import com.example.ui.components.CyberButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonPurple

@Composable
fun AdminProfileScreen(
    isArabic: Boolean,
    credentials: AdminCredentials,
    onUpdateProfile: (String, String, String, (Boolean, String) -> Unit) -> Unit,
    onBackClick: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newUsername by remember { mutableStateOf(credentials.username) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isCurrentPassVisible by remember { mutableStateOf(false) }
    var isNewPassVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
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
                    text = if (isArabic) "حساب ودخول الأدمن 👤" else "Admin Profile & Security 👤",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Shield Icon Banner
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(CyberNeonPink.copy(alpha = 0.15f))
                    .border(2.dp, CyberNeonPink, CircleShape)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = CyberNeonPink,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = credentials.username,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = if (isArabic) "مدير النظام المتكامل" else "System Administrator",
                fontSize = 12.sp,
                color = CyberNeonCyan,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = CyberNeonCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isArabic) "تعديل بيانات الحساب وكلمة السر 🔑" else "Change Credentials 🔑",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // New Username
                    Text(
                        text = if (isArabic) "اسم المستخدم الجديد" else "New Username",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = CyberNeonPink)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Current Password
                    Text(
                        text = if (isArabic) "كلمة المرور الحالية (تأكيد الأمان)" else "Current Password",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = CyberNeonCyan)
                        },
                        trailingIcon = {
                            IconButton(onClick = { isCurrentPassVisible = !isCurrentPassVisible }) {
                                Icon(
                                    imageVector = if (isCurrentPassVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = CyberNeonCyan
                                )
                            }
                        },
                        visualTransformation = if (isCurrentPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // New Password
                    Text(
                        text = if (isArabic) "كلمة المرور الجديدة (اختياري)" else "New Password (Optional)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = CyberNeonPurple)
                        },
                        trailingIcon = {
                            IconButton(onClick = { isNewPassVisible = !isNewPassVisible }) {
                                Icon(
                                    imageVector = if (isNewPassVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = CyberNeonPurple
                                )
                            }
                        },
                        visualTransformation = if (isNewPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Confirm New Password
                    Text(
                        text = if (isArabic) "تأكيد كلمة المرور الجديدة" else "Confirm New Password",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = CyberNeonPurple)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage!!,
                            fontSize = 12.sp,
                            color = CyberNeonPink,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    CyberButton(
                        text = if (isArabic) "حفظ التغييرات 🔒" else "Save Security Changes 🔒",
                        onClick = {
                            if (currentPassword.isBlank()) {
                                errorMessage = if (isArabic) "يرجى إدخال كلمة المرور الحالية للتحقق" else "Enter current password"
                                return@CyberButton
                            }
                            if (newPassword.isNotBlank() && newPassword != confirmPassword) {
                                errorMessage = if (isArabic) "كلمة المرور الجديدة غير متطابقة" else "Passwords do not match"
                                return@CyberButton
                            }

                            errorMessage = null
                            onUpdateProfile(currentPassword, newUsername, newPassword) { success, msg ->
                                if (!success) {
                                    errorMessage = msg
                                } else {
                                    currentPassword = ""
                                    newPassword = ""
                                    confirmPassword = ""
                                }
                            }
                        },
                        primaryColor = CyberNeonPink,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
