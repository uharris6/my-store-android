package com.uharris.mystore.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.uharris.mystore.R
import com.uharris.mystore.ui.theme.Typography

@Composable
fun ProductAmountHolder(
    amountInCart: Int,
    onDecreaseClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onDecreaseClick
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_remove),
                contentDescription = ""
            )
        }
        Text(
            text = amountInCart.toString(),
            style = Typography.labelLarge
        )
        IconButton(
            onClick = onIncreaseClick
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "")
        }
    }
}