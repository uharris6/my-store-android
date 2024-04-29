package com.uharris.mystore.presentation.cart

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.uharris.mystore.R
import com.uharris.mystore.domain.model.Product
import com.uharris.mystore.ui.components.ProductAmountHolder
import com.uharris.mystore.ui.components.TopBar
import com.uharris.mystore.ui.theme.MyStoreTheme
import com.uharris.mystore.ui.theme.Typography
import com.uharris.mystore.utils.toCurrency
import kotlinx.coroutines.Dispatchers

@Composable
fun CartScreenStateful(
    viewModel: CartViewModel
) {
    val state by viewModel.state.collectAsState()

    CartScreen(state = state, onEvent = viewModel::sendEvent)
}

@Composable
fun CartScreen(
    state: CartState,
    onEvent: (CartEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.cart),
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                onClick = { onEvent(CartEvent.OnBackClick) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(state.cartProducts, key = { it.id }) {
                    CartProductItem(
                        context = LocalContext.current,
                        product = it,
                        onDeleteClick = {
                            onEvent(CartEvent.OnDeleteProductClick(it))
                        },
                        onDecreaseClick = {
                            onEvent(
                                CartEvent.OnDecreaseAmountInCart(
                                    it.copy(amountInCart = it.amountInCart - 1)
                                )
                            )
                        },
                        onIncreaseClick = {
                            onEvent(
                                CartEvent.OnIncreaseAmountInCart(
                                    it.copy(amountInCart = it.amountInCart + 1)
                                )
                            )
                        }
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    ),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.total_amount,
                            state.totalAmount.toCurrency()
                        ),
                        style = Typography.titleLarge
                    )
                    Button(
                        modifier = Modifier
                            .padding(16.dp)
                            .width(200.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(4.dp),
                        onClick = { }
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource(id = R.string.purchase),
                            style = Typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartProductItem(
    context: Context,
    product: Product,
    onDeleteClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(product.image)
                        .dispatcher(Dispatchers.IO)
                        .memoryCacheKey(product.image)
                        .diskCacheKey(product.image)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = product.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = Typography.titleMedium
                    )
                    Text(
                        text = product.price.toCurrency(),
                        style = Typography.labelMedium
                    )
                    Button(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.Black
                        ),
                        onClick = onDeleteClick
                    ) {
                        Text(
                            text = stringResource(id = R.string.delete),
                            style = Typography.bodyMedium
                        )
                    }
                    ProductAmountHolder(
                        amountInCart = product.amountInCart,
                        onDecreaseClick = onDecreaseClick,
                        onIncreaseClick = onIncreaseClick
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun CartScreenPreview() {
    MyStoreTheme {
        CartScreen(
            state = CartState.initial().copy(
                cartProducts = listOf(
                    Product(
                        id = 1,
                        title = "Title 1",
                        description = "Description 1",
                        image = "",
                        rate = 3.5,
                        price = 1990.99,
                        amountInCart = 2
                    ),
                    Product(
                        id = 2,
                        title = "Title 2",
                        description = "Description 2",
                        image = "",
                        rate = 3.9,
                        price = 299.99,
                        amountInCart = 1
                    ),
                    Product(
                        id = 3,
                        title = "Title 3",
                        description = "Description 1",
                        image = "",
                        rate = 3.5,
                        price = 1990.99,
                        amountInCart = 2
                    )
                ),
                totalAmount = 2500.0
            ),
            onEvent = {}
        )
    }
}