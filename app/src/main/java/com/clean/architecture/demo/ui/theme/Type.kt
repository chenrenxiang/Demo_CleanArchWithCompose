package com.clean.architecture.demo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.clean.architecture.demo.R

// Set of Material typography styles to start with
val NotoSansJp = FontFamily(
    Font(R.font.noto_sans_jp_light, FontWeight.Light),
    Font(R.font.noto_sans_jp_medium, FontWeight.Medium),
    Font(R.font.noto_sans_jp_regular, FontWeight.Normal),
    Font(R.font.noto_sans_jp_bold, FontWeight.Bold),
    Font(R.font.noto_sans_jp_black, FontWeight.Black)
)

val Typography = Typography(

    titleLarge = TextStyle(
        fontFamily = NotoSansJp,
        fontWeight = FontWeight.Black
    ),
    titleMedium = TextStyle(
        fontFamily = NotoSansJp,
        fontWeight = FontWeight.Bold
    ),
    bodyLarge = TextStyle(
        fontFamily = NotoSansJp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontFamily = NotoSansJp,
        fontWeight = FontWeight.Medium
    ),
    bodySmall = TextStyle(
        fontFamily = NotoSansJp,
        fontWeight = FontWeight.Light
    )


)
