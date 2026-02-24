package com.clean.architecture.demo.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clean.architecture.demo.R


@Composable
fun TextWithLeadingLine(
    text: String,
    modifier: Modifier = Modifier,
    texColor: Color = MaterialTheme.colorScheme.onSurface,
    lineColor: Color = MaterialTheme.colorScheme.primary,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .width(4.dp)
                .height(16.dp)
                .background(lineColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = texColor,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextWithLeadingLinePreview() {
    TextWithLeadingLine(stringResource(R.string.agree_with_agreement))
}