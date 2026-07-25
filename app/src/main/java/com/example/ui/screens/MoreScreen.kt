package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonPurple
import com.example.utils.WhatsAppHelper

@Composable
fun MoreScreen(
    isDarkMode: Boolean,
    isArabic: Boolean,
    onToggleDarkMode: () -> Unit,
    onToggleLanguage: () -> Unit,
    onAdminClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var activeExpandedSection by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // App Info Banner Card
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_neova_logo_1785004031611),
                    contentDescription = "NEOVA STORE",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "𝑵𝑬𝑶𝑽𝑨 𝑺𝑻𝑶𝑹𝑬",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CyberNeonPink
                    )
                    Text(
                        text = if (isArabic) "الإصدار 1.0.0 (Production Ready)" else "Version 1.0.0 (Production Ready)",
                        fontSize = 12.sp,
                        color = CyberNeonCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Settings Section
        Text(
            text = if (isArabic) "إعدادات التطبيق" else "App Settings",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Dark Mode Toggle Row
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = null,
                        tint = CyberNeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isArabic) "تغيير الوضع الليلي" else "Night / Light Mode",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { onToggleDarkMode() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = CyberNeonPink,
                        uncheckedThumbColor = Color.Gray,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Language Switch Row
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onToggleLanguage
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = CyberNeonPink,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isArabic) "تغيير اللغة" else "Change Language",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = if (isArabic) "العربية 👈 EN" else "English 👈 عربي",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberNeonCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Admin Portal Entry Button
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAdminClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = CyberNeonPink,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isArabic) "لوحة أدمن النظام 🔐" else "Admin Portal 🔐",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = if (isArabic) "دخول 👈" else "Login 👈",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CyberNeonPink
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Information & Help Section Header
        Text(
            text = if (isArabic) "المعلومات والمساعدة" else "Information & Support",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 1. طريقة الاستخدام (How to Use)
        ExpandableInfoCard(
            id = "usage",
            title = if (isArabic) "طريقة الاستخدام" else "How to Use",
            icon = Icons.Default.MenuBook,
            activeSection = activeExpandedSection,
            onToggle = { activeExpandedSection = if (activeExpandedSection == "usage") null else "usage" }
        ) {
            Text(
                text = if (isArabic)
                    "1. تصفح قائمة الخدمات واختر الخدمة المطلوبة.\n2. اضغط على 'اطلب الآن' وأدخل اسمك ورقم هاتفك والـ ID.\n3. قم بتحويل المبلغ عبر فودافون كاش أو اتصالات كاش أو انستا باي.\n4. أرسل صورة التحويل عبر واتساب وسيم التنفيذ فوراً!"
                else
                    "1. Browse services & select your target top-up.\n2. Tap 'Order Now' & fill in name, phone & player ID.\n3. Transfer total cost via Vodafone Cash, Etisalat Cash or InstaPay.\n4. Send transfer screenshot on WhatsApp for instant execution!",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. من نحن (About Us)
        ExpandableInfoCard(
            id = "about",
            title = if (isArabic) "من نحن" else "About Us",
            icon = Icons.Default.Info,
            activeSection = activeExpandedSection,
            onToggle = { activeExpandedSection = if (activeExpandedSection == "about") null else "about" }
        ) {
            Text(
                text = if (isArabic)
                    "تطبيق NEOVA STORE هو المتجر الأول المتخصص في شحن الألعاب الإلكترونية، اشتراكات التطبيقات، وخدمات السوشيال ميديا في مصر والوطن العربي بأعلى معايير الأمان والسرعة."
                else
                    "NEOVA STORE is the leading hub for gaming recharges, app subscriptions, and social media services in Egypt & MENA with top safety and lightning delivery.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. الأسئلة الشائعة (FAQ)
        ExpandableInfoCard(
            id = "faq",
            title = if (isArabic) "الأسئلة الشائعة" else "FAQ",
            icon = Icons.Default.HelpOutline,
            activeSection = activeExpandedSection,
            onToggle = { activeExpandedSection = if (activeExpandedSection == "faq") null else "faq" }
        ) {
            Text(
                text = if (isArabic)
                    "• كم يستغرق وقت تنفيذ الطلب؟\nيتم تنفيذ الطلب خلال 5 إلى 15 دقيقة فور استلام صورة تحويل المبلغ.\n\n• هل الشحن آمن ومضمون؟\nنعم، جميع عمليات الشحن تتم رسمياً وبشكل مباشر 100%."
                else
                    "• How long does order execution take?\n5 to 15 minutes upon receiving payment screenshot.\n\n• Is recharge safe?\nYes, 100% official and direct top-up.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 4. سياسة الخصوصية (Privacy Policy)
        ExpandableInfoCard(
            id = "privacy",
            title = if (isArabic) "سياسة الخصوصية" else "Privacy Policy",
            icon = Icons.Default.Lock,
            activeSection = activeExpandedSection,
            onToggle = { activeExpandedSection = if (activeExpandedSection == "privacy") null else "privacy" }
        ) {
            Text(
                text = if (isArabic)
                    "نحن نحترم خصوصية جميع العملاء ونلتزم بحماية كافة بيانات الحسابات وأرقام الهواتف وعدم مشاركتها مع أي أطراف خارجية."
                else
                    "We respect user privacy and guarantee full protection of your account data and numbers without sharing with third parties.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 5. الشروط والأحكام (Terms & Conditions)
        ExpandableInfoCard(
            id = "terms",
            title = if (isArabic) "الشروط والأحكام" else "Terms & Conditions",
            icon = Icons.Default.Gavel,
            activeSection = activeExpandedSection,
            onToggle = { activeExpandedSection = if (activeExpandedSection == "terms") null else "terms" }
        ) {
            Text(
                text = if (isArabic)
                    "يرجى التأكد من إدخال الـ ID أو رابط الحساب الصحيح قبل إرسال الطلب. المتجر غير مسؤول عن إدخال بيانات خاطئة من قبل العميل."
                else
                    "Please double-check your ID or account link before submitting. The store is not responsible for incorrect user inputs.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 6. تواصل معنا (Contact Us)
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = { WhatsAppHelper.openSupportWhatsApp(context) }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.HeadsetMic,
                        contentDescription = null,
                        tint = CyberNeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isArabic) "تواصل معنا عبر واتساب" else "Contact Us on WhatsApp",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "201094750888",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberNeonPink
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ExpandableInfoCard(
    id: String,
    title: String,
    icon: ImageVector,
    activeSection: String?,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    val isExpanded = activeSection == id

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = CyberNeonPink,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = CyberNeonCyan
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    content()
                }
            }
        }
    }
}
