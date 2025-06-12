package com.example.skripsi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.skripsi.R

val PlusJakartaSans = FontFamily(
    Font(R.font.plusjkt_regular, FontWeight.Normal),
    Font(R.font.plusjkt_semibold, FontWeight.SemiBold),
    Font(R.font.plusjkt_bold, FontWeight.Bold)
)

val Mattone = FontFamily(
    Font(R.font.mattone, FontWeight.Normal)
)

val AppTypography = Typography(

    headlineSmall = TextStyle(
        fontFamily = Mattone,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = Mattone,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),

    titleLarge = TextStyle(
        fontFamily =  PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),

    titleMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    bodySmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),

)