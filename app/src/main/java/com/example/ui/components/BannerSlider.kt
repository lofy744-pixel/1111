package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.models.BannerItem
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonPink
import kotlinx.coroutines.delay

@Composable
fun BannerSlider(
    banners: List<BannerItem>,
    isArabic: Boolean,
    onBannerClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (banners.isEmpty()) return

    var currentIndex by remember { mutableStateOf(0) }

    // Auto auto-scroll effect
    LaunchedEffect(banners.size) {
        while (banners.isNotEmpty()) {
            delay(4000)
            currentIndex = (currentIndex + 1) % banners.size
        }
    }

    val currentBanner = banners[currentIndex]

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onBannerClick(currentBanner.targetCategoryId) }
    ) {
        // Banner Image
        if (currentBanner.imageUrl == "img_banner_gaming") {
            Image(
                painter = painterResource(id = R.drawable.img_banner_gaming_1785004043618),
                contentDescription = currentBanner.getTitle(isArabic),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (currentBanner.imageUrl == "img_banner_services") {
            Image(
                painter = painterResource(id = R.drawable.img_banner_services_1785004055997),
                contentDescription = currentBanner.getTitle(isArabic),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AsyncImage(
                model = currentBanner.imageUrl,
                contentDescription = currentBanner.getTitle(isArabic),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Overlay Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Banner Content Text
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = currentBanner.getTitle(isArabic),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentBanner.getSubtitle(isArabic),
                color = CyberNeonCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Indicator Dots
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            banners.forEachIndexed { index, _ ->
                val isActive = index == currentIndex
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .height(8.dp)
                        .width(if (isActive) 20.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (isActive) CyberNeonPink else Color.White.copy(alpha = 0.5f))
                )
            }
        }
    }
}
