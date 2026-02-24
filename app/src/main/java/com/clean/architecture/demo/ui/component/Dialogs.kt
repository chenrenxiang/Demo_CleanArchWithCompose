package com.clean.architecture.demo.ui.component

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.clean.architecture.demo.R
import com.clean.architecture.demo.app.MainActivity
import com.clean.architecture.demo.common.OnClick
import com.clean.architecture.demo.common.OnResult
import com.clean.architecture.demo.common.AuthenticationDialog
import com.clean.architecture.demo.ui.theme.MyDemoTheme
import kotlinx.coroutines.delay

@Composable
fun SystemVerifyDialog(
    context: Context,
    cancelled: OnResult<Unit>? = null,
    failed: OnResult<String>? = null,
    success: OnResult<Unit>,
) {
    AuthenticationDialog(
        activity = context as MainActivity,
        title = "生体認証の登録",
        subTitle = "この機能を利用するためには認証が必要です。",
        onSuccess = success,
        onUserCancelled = cancelled,
        onFailure = failed,
        onAuthUnavailable = success
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppAlertDialog(
    message: String,
    title: String = stringResource(R.string.confirm),
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(12.dp)
        ),
        onDismissRequest = { }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = message,
                fontSize = 14.sp
            )
            SpacerHeight(12)
            ConfirmButton(modifier = Modifier.fillMaxWidth(), text = "OK") {
                onDismissRequest()
            }
        }
    }
}


@Composable
fun DelayLoadingDialog(
    isLoading: Boolean,
    delayMillis: Long = 500L
) {
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(isLoading) {
        if(isLoading){
            showDialog = false
            delay(delayMillis)
            showDialog = true
        } else {
            showDialog = false
        }
    }
    if (showDialog) {
        LoadingDialog()
    }
}

@Composable
fun LoadingDialog(onDismissed: OnResult<Unit>? = null) {
    Dialog(onDismissRequest = { onDismissed?.invoke(Unit) }) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ConfirmDialog(
    messageText: String,
    confirmButtonText: String = stringResource(R.string.confirm),
    cancelButtonText: String = stringResource(R.string.cancel),
    onDismiss: OnClick,
    onConfirm: OnClick
) {
    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(25.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            )
            {
                SpacerHeight(40)

                Text(
                    modifier = Modifier.padding(horizontal = 30.dp),
                    text = messageText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp
                    ),
                )
                SpacerHeight(30)
                Row(
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.fillMaxHeight().width(140.dp)
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = cancelButtonText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 14.sp
                            ),
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxHeight().width(140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(colorResource(R.color.content_bg))
                            .clickable(onClick = onConfirm),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = confirmButtonText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 14.sp
                            ),
                        )
                    }
                }
                SpacerHeight(40)
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun DialogPreview() {
    MyDemoTheme(darkTheme = false) {
        Box(modifier = Modifier.fillMaxSize()){
            ConfirmDialog(
                messageText = "ファミリーを追加しますか？",
                confirmButtonText = stringResource(R.string.add),
                onDismiss = {},
                onConfirm = {}
            )
        }
    }

}