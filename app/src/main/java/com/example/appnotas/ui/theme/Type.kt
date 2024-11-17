package com.example.appnotas.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val typography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
)
