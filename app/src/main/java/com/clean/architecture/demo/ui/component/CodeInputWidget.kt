package com.clean.architecture.demo.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clean.architecture.demo.R
import com.clean.architecture.demo.common.log


@Composable
fun CodeCell(modifier: Modifier = Modifier, char: Char?, fontSize: TextUnit) {
    Card(
        modifier = modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2f.dp),
        shape = RoundedCornerShape(6.dp)

    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = char?.toString() ?: "",
                fontSize = fontSize,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@Composable
fun CodeInputWidget(
    modifier: Modifier = Modifier,
    length: Int = 4,
    space: Dp = 6.dp,
    width: Dp,
    height: Dp,
    onValueChanged: (String) -> Unit
) {
    var value by remember { mutableStateOf("") }
    val fontSize = ((width.value - (length - 1) * space.value) / length).sp * 0.8f
    log("CodeInputWidget", "width = $width, fontSize = $fontSize")
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = { input ->
            val filtered = input.filter { it.isDigit() }.take(length)
            value = filtered
            onValueChanged(value)
        },

        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .background(
                        color = colorResource(R.color.content_bg),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(15.dp),
                horizontalArrangement = Arrangement.spacedBy(space)
            ) {
                repeat(length) { index ->
                    CodeCell(
                        modifier = Modifier.weight(1f),
                        char = value.getOrNull(index),
                        fontSize = fontSize
                    )
                }
            }
        }
    )
}


@Composable
@Preview(showBackground = true)
fun CodeCellPreview() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White), contentAlignment = Alignment.Center) {
        CodeInputWidget(width = 300.dp, height = 100.dp) {  }
    }

}