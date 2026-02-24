package com.clean.architecture.demo.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clean.architecture.demo.R



@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(keyboardType != KeyboardType.Password) }

    Surface(
        modifier = modifier.height(56.dp).fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 2.dp
    ) {
        TextField(
            value = text,
            onValueChange = {
                onValueChange(it.replace("\n", ""))
            },
            modifier = modifier.fillMaxSize()
                .heightIn(min = 56.dp, max = 56.dp)
                .background(color = Color.Transparent),
            placeholder = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 12.sp,
                        color = colorResource(R.color.color_unable)
                    ),
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if(passwordVisible){
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                if (keyboardType == KeyboardType.Password) {
                    val icon = if(passwordVisible){
                        R.drawable.ic_visibility_24
                    } else {
                        R.drawable.ic_visibility_off_24
                    }
                    val iconColor = if(passwordVisible) R.color.black_50 else R.color.color_unable
                    //val iconColor = colorResource()
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                             painter = painterResource(id = icon),
                            contentDescription = null,
                            tint = colorResource(iconColor)
                        )
                    }
                }
            },
            textStyle = MaterialTheme.typography.titleMedium.copy(
                fontSize = 15.sp
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TextFieldWithLabelPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AppTextField(
            modifier = Modifier.padding(horizontal = 15.dp),
            label = "Email",
            text = "",)
        {

        }
    }

}