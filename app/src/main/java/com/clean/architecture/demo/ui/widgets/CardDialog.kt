package com.clean.architecture.demo.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.clean.architecture.demo.R
import com.clean.architecture.demo.ui.theme.MyDemoTheme

val cardPadding = 20.dp

@Composable
fun CardDialog(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    bottom: @Composable () -> Unit,
) {

    CardWithBottomShadow {
        Column(
            modifier = modifier.fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(
                        topStart = 25.dp,
                        topEnd = 25.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    )
                )
                .padding(horizontal = cardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpacerHeight(35)
            Image(
                painter = painterResource(R.drawable.svg_close),
                contentDescription = null,
                modifier = Modifier.size(height = 16.dp, width = 150.dp)
            )
            SpacerHeight(30)
            content()
            SpacerHeight(30)
        }

        SpacerHeight(2)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(85.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 4.dp,
                        bottomStart = 25.dp,
                        bottomEnd = 25.dp
                    )
                )
                .background(
                    color = Color.White.copy(alpha = 0.9f),
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            bottom()
        }

    }
}


@Composable
fun CardDialogWithoutBottom(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    CardWithBottomShadow {
        Column(
            modifier = modifier.fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(25.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpacerHeight(30)
            content()
            SpacerHeight(30)
        }
    }
}


@Composable
fun CardWithBottomShadow(
    modifier: Modifier = Modifier,
    shadowOffsetY: Dp = 52.dp,
    content: @Composable ColumnScope.() -> Unit
) {

    Column(modifier = modifier) {
        SpacerHeight(10)
        Box(modifier = modifier.padding(bottom = 20.dp)) {
            //shadow
            Image(
                painter = painterResource(R.drawable.shadow),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .offset(y = shadowOffsetY),
            )
            
            //content
            Column(content = content)
        }
    }

}

@Composable
@Preview(showBackground = true)
fun CardDialogPreview() {
    MyDemoTheme(darkTheme = false) {
//        AgreementLayout()
        CardDialogWithoutBottom {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp))
        }
    }

}

