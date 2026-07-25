package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.models.CategoryItem
import com.example.models.ServiceItem
import com.example.ui.components.CyberButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonPurple

@Composable
fun ServicesScreen(
    services: List<ServiceItem>,
    categories: List<CategoryItem>,
    searchQuery: String,
    selectedCategoryId: String?,
    isArabic: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelect: (String?) -> Unit,
    onServiceClick: (ServiceItem) -> Unit,
    onOrderNowClick: (ServiceItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = if (isArabic) "ابحث عن خدمة، لعبة، أو اشتراك..." else "Search for games, apps...",
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = CyberNeonPink
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberNeonPink,
                unfocusedBorderColor = CyberNeonCyan.copy(alpha = 0.4f),
                focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Categories Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 8.dp)
        ) {
            // All Chip
            item(key = "all_chip") {
                val isSelected = selectedCategoryId == null
                CategoryChip(
                    title = if (isArabic) "الكل" else "All",
                    icon = Icons.Default.Widgets,
                    isSelected = isSelected,
                    onClick = { onCategorySelect(null) }
                )
            }

            items(categories, key = { it.id }) { category ->
                val isSelected = selectedCategoryId == category.id
                val icon = getCategoryIcon(category.iconName)
                CategoryChip(
                    title = category.getName(isArabic),
                    icon = icon,
                    isSelected = isSelected,
                    onClick = { onCategorySelect(category.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Services List
        if (services.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isArabic) "لا توجد خدمات مطابقة للبحث حالياً" else "No matching services found",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(services, key = { it.id }) { service ->
                    ServiceCard(
                        service = service,
                        isArabic = isArabic,
                        onServiceClick = { onServiceClick(service) },
                        onOrderNowClick = { onOrderNowClick(service) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) CyberNeonPink else MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
    val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = textColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ServiceCard(
    service: ServiceItem,
    isArabic: Boolean,
    onServiceClick: () -> Unit,
    onOrderNowClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onServiceClick
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Service Icon / Image
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CyberNeonPink.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (service.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = service.imageUrl,
                            contentDescription = service.getName(isArabic),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        val icon = getCategoryIconForService(service.categoryId)
                        Icon(
                            imageVector = icon,
                            contentDescription = service.getName(isArabic),
                            tint = CyberNeonPink,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Name & Price EGP
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = service.getName(isArabic),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${service.priceEgp} ${if (isArabic) "ج.م" else "EGP"}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CyberNeonCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Short Description
            Text(
                text = service.getDescription(isArabic),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Min & Max Limits
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "الحد الأدنى: ${service.minLimit} | الأقصى: ${service.maxLimit}"
                    else "Min: ${service.minLimit} | Max: ${service.maxLimit}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )

                // Order Now Button
                CyberButton(
                    text = if (isArabic) "اطلب الآن 🚀" else "Order Now 🚀",
                    onClick = onOrderNowClick,
                    icon = Icons.Default.ShoppingCart,
                    gradientColors = listOf(CyberNeonPink, CyberNeonPurple),
                    modifier = Modifier.width(130.dp).height(40.dp)
                )
            }
        }
    }
}

private fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName) {
        "gamepad" -> Icons.Default.Gamepad
        "apps" -> Icons.Default.Apps
        "share" -> Icons.Default.Share
        else -> Icons.Default.Widgets
    }
}

private fun getCategoryIconForService(categoryId: String): ImageVector {
    return when (categoryId) {
        "cat_games" -> Icons.Default.Gamepad
        "cat_apps" -> Icons.Default.Apps
        "cat_social" -> Icons.Default.Share
        else -> Icons.Default.Widgets
    }
}
