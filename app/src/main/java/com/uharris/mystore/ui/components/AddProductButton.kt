package com.uharris.mystore.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddProductButton(
    amountInCart: Int,
    isInCart: Boolean,
    onClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isInCart) {
        ProductAmountHolder(
            amountInCart = amountInCart,
            onDecreaseClick = onDecreaseClick,
            onIncreaseClick = onIncreaseClick,
            modifier = modifier
        )
    } else {
        IconButton(
            modifier = modifier
                .clip(CircleShape)
                .size(32.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Black
            ),
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = Color.White,
                contentDescription = "Agregar"
            )
        }
    }
}