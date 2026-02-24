package com.clean.architecture.demo.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clean.architecture.demo.R
import com.clean.architecture.demo.common.OnClick


@Composable
fun ConfirmButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    roundCornerSize: Int = 8,
    minHeight: Dp = 48.dp,
    alpha: Float = 1f,
    fontSize: TextUnit = 0.sp,
    onClick: () -> Unit
) {

    val primaryColor = MaterialTheme.colorScheme.primary.copy(alpha = alpha)
    val disabledColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.defaultMinSize(minHeight = minHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) primaryColor else disabledColor,
            contentColor = Color.White,
            disabledContentColor = Color.White.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(roundCornerSize.dp)
    ) {
        Text(
            text = text,
            style = if(fontSize == 0.sp) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.titleMedium.copy(fontSize = fontSize)
            }
        )
    }

}

@Composable
fun RoundedCornerConfirmButton(
    text: String,
    modifier: Modifier = Modifier,
    rounderCornerSize: Int = 24,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    ConfirmButton(
        text = text,
        modifier = modifier,
        enabled = enabled,
        roundCornerSize = rounderCornerSize,
        onClick = onClick
    )
}

@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: OnClick) {
    Box(
        modifier = modifier.size(60.dp, 60.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = modifier.clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
            text = stringResource(R.string.back),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 14.sp
            ),
            textAlign = TextAlign.Center
        )
    }

}

@Preview
@Composable
fun ConfirmButtonPreview() {
    RoundedCornerConfirmButton(text = "next", enabled = true) {

    }
}